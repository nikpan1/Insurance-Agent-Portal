import { Component } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

type ClientRegistrationForm = FormGroup<{
  firstName: FormControl<string>;
  lastName: FormControl<string>;
  email: FormControl<string>;
  pesel: FormControl<string>;
}>;

type RegistrationControlName = keyof ClientRegistrationForm['controls'];

@Component({
  selector: 'app-client-registration',
  imports: [ReactiveFormsModule],
  templateUrl: './client-registration.component.html',
  styleUrl: './client-registration.component.scss'
})
export class ClientRegistrationComponent {
  protected readonly registrationForm: ClientRegistrationForm = new FormGroup({
    firstName: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required]
    }),
    lastName: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required]
    }),
    email: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.email]
    }),
    pesel: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.pattern(/^\d{11}$/)]
    })
  });

  protected isInvalid(controlName: RegistrationControlName): boolean {
    const control = this.registrationForm.controls[controlName];
    return control.invalid && (control.dirty || control.touched);
  }

  protected hasError(controlName: RegistrationControlName, errorName: string): boolean {
    const control = this.registrationForm.controls[controlName];
    return this.isInvalid(controlName) && control.hasError(errorName);
  }
}
