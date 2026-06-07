import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ClientRequestDto } from './client-request.dto';

@Injectable({
  providedIn: 'root'
})
export class ClientService {
  private readonly clientsApiUrl = '/api/clients';

  constructor(private readonly http: HttpClient) {}

  registerClient(payload: ClientRequestDto): Observable<void> {
    return this.http.post<void>(this.clientsApiUrl, payload);
  }
}
