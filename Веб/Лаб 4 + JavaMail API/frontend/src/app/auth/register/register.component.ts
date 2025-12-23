import { Component, ChangeDetectorRef } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink]
})
export class RegisterComponent {
  registerForm!: FormGroup;

  formError: string | null = null;
  successMessage: string | null = null;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private cdr: ChangeDetectorRef,
  ) {
    this.registerForm = this.fb.group({
      login: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      passwordConfirm: ['', Validators.required],
      passwordHint: ['']
    });
  }

  get passwordsMismatch(): boolean {
    const password = this.registerForm.get('password')?.value;
    const confirm = this.registerForm.get('passwordConfirm')?.value;
    return password !== confirm;
  }

  isFormValid(): boolean {
    return this.registerForm.valid && !this.passwordsMismatch;
  }

  register() {
    this.formError = null;
    this.successMessage = null;

    if (!this.isFormValid()) {
      this.formError = this.passwordsMismatch
        ? 'Пароли не совпадают.'
        : 'Исправьте ошибки формы.';
      this.cdr.detectChanges();
      return;
    }

    const { login, email, password, passwordHint } = this.registerForm.value;
    this.authService.register(login!, email!, password!, passwordHint || '')
      .subscribe({
        next: () => {
          this.formError = null;
          this.successMessage = 'Проверьте email для подтверждения регистрации!';
          this.cdr.detectChanges();

        },
        error: (err) => {
          console.error('Ошибка регистрации', err);
          this.formError = err.error?.error || 'Ошибка регистрации. Попробуйте позже.';
          this.cdr.detectChanges();
        }
      });
  }
}
