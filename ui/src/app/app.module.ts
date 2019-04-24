import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {HttpClientModule, HTTP_INTERCEPTORS} from '@angular/common/http';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {FormsModule} from '@angular/forms';
import {NgZorroAntdModule, NZ_I18N, zh_CN, NZ_MESSAGE_CONFIG, NZ_NOTIFICATION_CONFIG,NZ_ICONS } from 'ng-zorro-antd';
import {registerLocaleData} from '@angular/common';
import zh from '@angular/common/locales/zh';
import {AppComponent} from './app.component';
import {AppRoutingModule} from './app.routing';
import {Globals} from './utils/globals';
import {InterceptorService} from './common/services/InterceptorService';
import {CommonService} from './common/services/common.service';
import {CurdService} from './curd.service';
import {HeaderModule} from './common/header/header.module';
//mock
import { DelonMockModule } from '@delon/mock';
// 只对开发环境有效
import { environment } from '../environments/environment';

registerLocaleData(zh);

@NgModule({
    declarations: [
        AppComponent
    ],
    imports: [
        BrowserModule,
        FormsModule,
        HttpClientModule,
        BrowserAnimationsModule,
        AppRoutingModule,
        HeaderModule,
        NgZorroAntdModule.forRoot()
    ],
    providers: [
        {provide: NZ_I18N, useValue: zh_CN},
        {provide: HTTP_INTERCEPTORS, useClass: InterceptorService, multi: true},
        {provide: NZ_MESSAGE_CONFIG, useValue: {nzDuration: 3000, nzMaxStack: 1}},
        {provide: NZ_NOTIFICATION_CONFIG, useValue: {nzDuration: 3000, nzMaxStack: 1}},
        Globals,
        CommonService,
        CurdService
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
}
