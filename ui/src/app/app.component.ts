import {Component} from '@angular/core';
import {Router, NavigationEnd} from '@angular/router';
import {CommonService} from './common/services/common.service';
import {fromEvent} from 'rxjs';
import {debounceTime} from 'rxjs/operators';
import {NzOptionContainerComponent} from 'ng-zorro-antd';
import {CurdService} from './curd.service';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.less'],
})
export class AppComponent {
    title = 'app';

    timeoutHandler: any;

    firstLevelMap: any = {
        '/cas-client': 1,
    };

    urlMap: any = {
        '/cas-client': 'SSO集成DEMO',
    };

    constructor(private router: Router,
                private curdService: CurdService,
                private commonService: CommonService) {

        const that = this;
        NzOptionContainerComponent.prototype.dropDownScroll = function (e, ul) {
            e.preventDefault();
            e.stopPropagation();

            let count = ul.scrollHeight - ul.scrollTop - ul.clientHeight;
            if (ul && (Math.abs(count) < 2)) {
                if (that.timeoutHandler) {
                    clearTimeout(that.timeoutHandler);
                }
                that.timeoutHandler = setTimeout(() => {
                    this.nzScrollToBottom.emit();
                }, 100);
            }
        };
        this.getUrl();

        const _that = this;

        // 点击浏览器前进和后退按钮时处理abc
        window.addEventListener('popstate', function (a?) {

            // 没有历史数据不做处理
            if (!history.state) {
                return;
            }

            // 根据历史状态key，找到面包屑快照，更新面包屑
            if (_that.commonService.navMap[history.state.uuid] !== null) {
                _that.commonService.navList = _that.commonService.navMap[history.state.uuid];
            }
            // 传递状态，用于路由监听事件
            _that.commonService.navHistory = history.state.uuid;

        });

    }

    getUrl(){
        let url = null;
        if (parent !== window) { 
            try {
            url = parent.location.href; 
            }catch (e) { 
            url = document.referrer; 
            } 
        }
        if(url){
            const obj = url.split('/')[0] + "//"+ url.split('/')[2];
            localStorage.setItem('user_manage_report_url', obj);
        }
        return url;
     
    }
}
