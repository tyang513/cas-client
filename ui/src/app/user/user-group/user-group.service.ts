import {Injectable, Injector} from '@angular/core';
import {CurdService} from '../../curd.service';
import {Observable} from "rxjs/index";
import {catchError} from "rxjs/internal/operators";
import {error} from "util";

@Injectable()
export class UserGroupService extends CurdService {

    constructor(private injector: Injector) {
        super(injector);
        this.baseUrl = '/cas-client';
    }

    getHello(params?: any) {
        let url = `${this.baseUrl}/hello`;
        return this.http.get(url,params).pipe(
          catchError(this.handleError)
        );
    }
}
