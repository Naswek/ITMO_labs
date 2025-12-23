import { Component, ChangeDetectorRef } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink]
})
export class LoginComponent {

  loginForm!: FormGroup;

  hintMessage: string | null = null;
  errorMessage: string | null = null;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private cdr: ChangeDetectorRef,
  ) {
    this.loginForm = this.fb.group({
      identifier: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  get identifier() {
    return this.loginForm.get('identifier')?.value || '';
  }

  login() {
    if (this.loginForm.invalid) {
      this.errorMessage = 'Заполните логин и пароль.';
      this.cdr.detectChanges();
      return;
    }

    const { identifier, password } = this.loginForm.value;
    this.errorMessage = null;

    this.authService.login(identifier, password).subscribe({
      next: () => {
        this.errorMessage = null;
        this.cdr.detectChanges();
        this.router.navigate(['/main']);
      },
      error: err => {
        console.error('Ошибка входа', err);
        this.errorMessage = err.error?.error || 'Ошибка входа. Проверьте логин или пароль.';
        this.cdr.detectChanges();
      }
    });
  }

  async requestPasswordHint() {
    const identifier = this.loginForm.value.identifier?.trim();
    if (!identifier) {
      this.markIdentifierError();
      this.errorMessage = 'Укажите email или логин для подсказки пароля.';
      this.cdr.detectChanges();
      return;
    }

    this.errorMessage = null;
    this.cdr.detectChanges();

    const body = new URLSearchParams();
    body.set('identifier', identifier);

    try {
      const response = await fetch('http://localhost:8080/api/auth/password-hint', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: body.toString()
      });

      if (response.ok) {
        const data = await response.json();
        this.hintMessage = data.message;
        this.cdr.detectChanges();
        setTimeout(() => {
          this.hintMessage = null;
          this.cdr.detectChanges();
        }, 5000);
      } else {
        this.errorMessage = 'Не удалось отправить запрос на подсказку пароля.';
        this.cdr.detectChanges();
      }
    } catch (e) {
      console.error('Ошибка password-hint', e);
      this.errorMessage = 'Не удалось отправить запрос на подсказку пароля.';
      this.cdr.detectChanges();
    }
  }

  markIdentifierError() {
    const control = this.loginForm.get('identifier');
    control?.markAsTouched();
    control?.setErrors({ required: true });
  }
}
