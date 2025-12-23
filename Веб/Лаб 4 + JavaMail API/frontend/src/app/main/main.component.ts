import {
  AfterViewInit,
  Component,
  ElementRef,
  OnInit,
  ViewChild,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { ApiService } from '../services/api.service';
import { Result } from '../models/result.model';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';
import {switchMap} from 'rxjs/operators';
import { ChangeDetectorRef, NgZone } from '@angular/core';

@Component({
  selector: 'app-main',
  standalone: true,
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css'],
  imports: [CommonModule, ReactiveFormsModule],
})
export class MainComponent implements OnInit, AfterViewInit {
  @ViewChild('plot') plot!: ElementRef<HTMLCanvasElement>;

  plotForm!: FormGroup;
  results: Result[] = [];
  formError = '';

  xValues = [-2, -1.5, -1, -0.5, 0, 0.5, 1, 1.5, 2];
  rValues = [-2, -1.5, -1, -0.5, 0, 0.5, 1, 1.5, 2];

  sortColumn: 'x' | 'y' | 'r' | 'hit' | null = null;
  sortDirection: 'asc' | 'desc' = 'asc';

  pageSize = 10;
  currentPage = 1;
  pagesWindowSize = 5;

  get totalPages(): number {
    return Math.ceil((this.results?.length ?? 0) / this.pageSize) || 1;
  }

  get pagedResults(): Result[] {
    const start = (this.currentPage - 1) * this.pageSize;
    return (this.results ?? []).slice(start, start + this.pageSize);
  }

  readonly minVal = -5;
  readonly maxVal = 3;
  readonly scale = 55;

  private ctx!: CanvasRenderingContext2D;
  private width = 0;
  private height = 0;
  private centerX = 0;
  private centerY = 0;

  constructor(
    private fb: FormBuilder,
    private api: ApiService,
    private auth: AuthService,
    private router: Router,
    private zone: NgZone,
    private cdr: ChangeDetectorRef,
  ) {}

  ngOnInit(): void {
    this.plotForm = this.fb.group({
      x: [null, Validators.required],
      y: [
        0,
        [Validators.required, Validators.min(-5), Validators.max(3)],
      ],
      r: [null, Validators.required],
    });
    this.loadResults();

  }

  private getSelectedR(): number | null {
    const r = this.plotForm.value.r;
    return r === null || r === undefined ? null : Number(r);
  }

  ngAfterViewInit(): void {
    const canvas = this.plot.nativeElement;
    const ctx = canvas.getContext('2d');
    if (!ctx) {
      console.error('Не удалось получить 2D контекст');
      return;
    }
    this.ctx = ctx;

    this.width = (this.maxVal - this.minVal) * this.scale + 100;
    this.height = (this.maxVal - this.minVal) * this.scale + 100;
    canvas.width = this.width;
    canvas.height = this.height;

    this.centerX = -this.minVal * this.scale + 50;
    this.centerY = this.maxVal * this.scale + 50;

    this.redrawCanvas();
  }

  private redrawCanvas(): void {
    if (!this.ctx) return;

    this.ctx.clearRect(0, 0, this.width, this.height);
    this.drawAxes();
    this.drawArea();
    this.drawPointsFromResults();
  }

  private drawAxes(): void {
    const ctx = this.ctx;

    ctx.strokeStyle = 'white';
    ctx.lineWidth = 2;
    ctx.beginPath();
    ctx.moveTo(0, this.centerY);
    ctx.lineTo(this.width, this.centerY);
    ctx.moveTo(this.centerX, 0);
    ctx.lineTo(this.centerX, this.height);
    ctx.stroke();

    ctx.fillStyle = 'white';
    ctx.font = '12px Arial';
    ctx.textAlign = 'center';

    for (let i = this.minVal; i <= this.maxVal; i++) {
      const x = this.centerX + i * this.scale;
      const y = this.centerY - i * this.scale;

      ctx.beginPath();
      ctx.moveTo(x, this.centerY - 5);
      ctx.lineTo(x, this.centerY + 5);
      ctx.moveTo(this.centerX - 5, y);
      ctx.lineTo(this.centerX + 5, y);
      ctx.stroke();

      if (i !== 0) {
        ctx.fillText(i.toString(), x, this.centerY + 15);
        ctx.fillText(i.toString(), this.centerX + 15, y + 5);
      }
    }
  }

  private drawArea(): void {
    const ctx = this.ctx;
    const r = this.getSelectedR() ?? 1;
    const R = Math.abs(r);

    ctx.save();
    ctx.fillStyle = 'rgba(0,128,255,0.4)';
    ctx.globalAlpha = 0.4;

    ctx.fillRect(
      this.centerX,
      this.centerY - (R * this.scale) / 2,
      R * this.scale,
      (R * this.scale) / 2,
    );

    ctx.beginPath();
    ctx.moveTo(this.centerX, this.centerY);
    ctx.arc(
      this.centerX,
      this.centerY,
      R * this.scale,
      Math.PI,
      Math.PI * 1.5,
      false,
    );
    ctx.closePath();
    ctx.fill();

    ctx.beginPath();
    ctx.moveTo(this.centerX, this.centerY);
    ctx.lineTo(this.centerX - R * this.scale, this.centerY);
    ctx.lineTo(this.centerX, this.centerY + R * this.scale);
    ctx.closePath();
    ctx.fill();

    ctx.globalAlpha = 1;
    ctx.restore();
  }

  get visiblePages(): number[] {
    const windowIndex = Math.floor((this.currentPage - 1) / this.pagesWindowSize);
    const start = windowIndex * this.pagesWindowSize + 1;
    const end = Math.min(start + this.pagesWindowSize - 1, this.totalPages);

    const pages: number[] = [];
    for (let i = start; i <= end; i++) pages.push(i);
    return pages;
  }

  nextWindow(): void {
    const nextStart =
      Math.floor((this.currentPage - 1) / this.pagesWindowSize) * this.pagesWindowSize +
      this.pagesWindowSize +
      1;

    if (nextStart <= this.totalPages) {
      this.currentPage = nextStart;
      this.redrawCanvas();
    }
  }

  prevWindow(): void {
    const prevStart =
      Math.floor((this.currentPage - 1) / this.pagesWindowSize) * this.pagesWindowSize -
      this.pagesWindowSize +
      1;

    if (prevStart >= 1) {
      this.currentPage = prevStart;
      this.redrawCanvas();
    }
  }

  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages) return;
    this.currentPage = page;
    this.redrawCanvas();
  }

  private drawPointsFromResults(): void {
    const data = this.pagedResults;

    const ctx = this.ctx;

    const seen: Record<string, number> = {};
    const spreadPx = 7;

    data.forEach(p => {
      const key = `${p.x}_${p.y}`;
      const n = (seen[key] ?? 0);
      seen[key] = n + 1;

      const baseX = this.centerX + p.x * this.scale;
      const baseY = this.centerY - p.y * this.scale;

      let px = baseX;
      let py = baseY;

      if (n > 0) {
        const angle = (n * 2 * Math.PI) / 6;
        px = baseX + Math.cos(angle) * spreadPx;
        py = baseY + Math.sin(angle) * spreadPx;
      }

      ctx.beginPath();
      ctx.arc(px, py, 5, 0, 2 * Math.PI);
      ctx.fillStyle = p.hit ? 'green' : 'red';
      ctx.fill();
    });
  }

  onCanvasClick(event: MouseEvent): void {
    const r = this.getSelectedR();
    if (r === null) {
      this.formError = 'Сначала выберите R';
      return;
    }
    this.formError = '';

    const canvas = this.plot.nativeElement;
    const rect = canvas.getBoundingClientRect();
    const xPix = event.clientX - rect.left;
    const yPix = event.clientY - rect.top;

    let x = (xPix - this.centerX) / this.scale;
    let y = (this.centerY - yPix) / this.scale;

    const minX = -2, maxX = 2;
    const minY = -5, maxY = 3;

    if (x < minX) x = minX;
    if (x > maxX) x = maxX;

    if (y <= minY) y = minY + 0.001;
    if (y >= maxY) y = maxY - 0.001;

    const xVal = +x.toFixed(3);
    const yVal = +y.toFixed(3);

    this.plotForm.patchValue({ x: xVal, y: yVal });
    this.submit();
  }

  submit(): void {
    this.formError = '';

    const { x, y, r } = this.plotForm.value;
    const xNum = Number(x);
    const yNum = Number(y);
    const rNum = Number(r);

    if (x === null || x === undefined || isNaN(xNum)) {
      this.formError = 'Выберите X';
      return;
    }
    if (r === null || r === undefined || isNaN(rNum)) {
      this.formError = 'Выберите R';
      return;
    }
    if (y === null || y === undefined || y === '') {
      this.formError = 'Введите Y';
      return;
    }
    if (isNaN(yNum)) {
      this.formError = 'Y должно быть числом';
      return;
    }
    if (!(yNum >= -5 && yNum <= 3)) {
      this.formError = 'Y должен быть в диапазоне [-5; 3]';
      return;
    }

    this.api.submit(xNum, yNum, rNum).subscribe({
      next: () => this.loadResults(),
      error: err => {
        console.error('Ошибка submit', err);
        this.formError =
          err.status === 401
            ? 'Не авторизован (401): нужно залогиниться.'
            : 'Ошибка отправки (подробности в консоли).';
      },
    });
  }

  clear(): void {
    this.api.clearResults().subscribe({
      next: () => {
        this.results = [];
        this.currentPage = 1;
        this.redrawCanvas();
      },
      error: err => {
        console.error('Ошибка clear', err);
        this.formError =
          err.status === 401
            ? 'Не авторизован (401): нужно залогиниться.'
            : 'Ошибка очистки.';
      },
    });
  }

  loadResults(): void {
    this.api.getResults().subscribe({
      next: data => {
        this.zone.run(() => {
          this.results = [...data];
          this.redrawCanvas();
          this.cdr.detectChanges();
        });
      },
      error: err => {
        console.error('Ошибка getResults', err);
        this.formError =
          err.status === 401
            ? 'Не авторизован (401): нужно залогиниться.'
            : 'Не удалось загрузить результаты.';
      },
    });
  }

  logout(): void {
    this.auth.logout().subscribe({
      next: () => this.router.navigate(['/login']),
      error: err => {
        console.error('Ошибка logout', err);
        this.formError = 'Не удалось выйти из системы.';
      },
    });
  }

  onRChange(): void {
    this.redrawCanvas();
  }

  sortBy(column: 'x' | 'y' | 'r' | 'hit'): void {
    if (this.sortColumn === column) {
      this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
    } else {
      this.sortColumn = column;
      this.sortDirection = 'asc';
    }

    this.results = [...this.results].sort((a, b) => {
      let valA: any = a[column];
      let valB: any = b[column];

      if (typeof valA === 'boolean') {
        valA = valA ? 1 : 0;
        valB = valB ? 1 : 0;
      }

      if (valA < valB) return this.sortDirection === 'asc' ? -1 : 1;
      if (valA > valB) return this.sortDirection === 'asc' ? 1 : -1;
      return 0;
    });

    this.currentPage = 1;
    this.redrawCanvas();
  }

  getSortIcon(column: 'x' | 'y' | 'r' | 'hit'): string {
    if (this.sortColumn !== column) return '↕';
    return this.sortDirection === 'asc' ? '▲' : '▼';
  }
}
