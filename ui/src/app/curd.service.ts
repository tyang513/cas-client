import {Injectable, Injector} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Globals} from './utils/globals';
import {CommonService} from './common/services/common.service';
import {NzMessageService, NzNotificationService, NzModalService} from 'ng-zorro-antd';
import {throwError} from "rxjs/index";

@Injectable()
export class CurdService {

    public static that: CurdService;
    public baseUrl: string;
    public urlParams: any;
    token: any;

    protected http: HttpClient;
    protected globals: Globals;
    protected commonService: CommonService;
    protected message: NzMessageService;

    constructor(private baseInjector: Injector) {
        this.http = this.baseInjector.get(HttpClient);
        this.globals = this.baseInjector.get(Globals);
        this.commonService = this.baseInjector.get(CommonService);
        this.message = this.baseInjector.get(NzMessageService);

        CurdService.that = this;
    }

    public handleGood = (response: any) => {
        if (response.code === 200) {
            this.message.create('success', '操作成功');
        }
        return response;
    };
    public handleBad = (response: any) => {
        if (response.code !== 200) {
            this.message.create('error', response.message);
        }
        return response;
    };

  /**
   * 错误处理
   * @param error
   */
  public handleError(error: any) {
    if (error.status == 401 || error.status == 403) {
      CurdService.that.redirect(JSON.parse(error['error']));
      return throwError('登录超时。');
    } else {
      let err: string;
      try {
        let data = error.json();
        if (typeof data == 'object' && data.constructor == Array) {
          err = data[0];
        } else if (typeof data == 'string' && data.constructor == String) {
          err = data;
        } else {
          err = '页面错误，请稍后重试。';
        }
      } catch(e) {
        err = error.message || error;
      }
      return throwError(err);
    }
  }

  /**
   * 跳转登录
   * @param data
   */
  public redirect(data: any): void {
      //  根据不同环境配置不同 URL
      console.log(`${data.service}${document.location.origin}${data.redirect}`+
          document.location.href);
      document.location.href =
          `${data.service}${document.location.origin}${data.redirect}`+
          document.location.href;
  }

    /**
     * 传参公共方法
     * @param params
     */
    public getParams(params: any): string {
        const arr = [];
        for (const name in params) {
            if (params[name] != null) {
                arr.push(name + '=' + params[name]);
            }
        }
        return '?' + arr.join('&');
    }
}
