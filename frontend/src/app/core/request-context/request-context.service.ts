import { Injectable, Signal, signal } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class RequestContextService {
  private readonly userIdSignal = signal<number>(101);

  get currentUserId(): Signal<number> {
    return this.userIdSignal.asReadonly();
  }

  setCurrentUserId(userId: number): void {
    this.userIdSignal.set(userId);
  }
}
