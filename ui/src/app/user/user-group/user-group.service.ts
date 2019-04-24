import {Injectable, Injector} from '@angular/core';
import {CurdService} from '../../curd.service';
import {catchError} from "rxjs/internal/operators";
import {
    HttpHeaders
} from '@angular/common/http';

@Injectable()
export class UserGroupService extends CurdService {

    constructor(private injector: Injector) {
        super(injector);
        this.baseUrl = '/cas-client';
    }

    getHello(params?: any) {
        let url = `${this.baseUrl}/hello`;
        const obj = {};
        obj['Content-Type'] = 'application/json';

        return this.http.get(url,{headers: new HttpHeaders(obj),responseType: 'text'}).pipe(
          catchError(this.handleError)
        );
    }
}
