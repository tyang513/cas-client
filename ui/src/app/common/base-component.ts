import {Injector, OnInit, OnChanges, SimpleChanges} from '@angular/core';
import {Router, ActivatedRoute} from '@angular/router';
import {CommonService} from './services/common.service';
import {NzMessageService, NzNotificationService, NzModalService} from 'ng-zorro-antd';
import {Globals} from '../utils/globals';

export class BaseComponent implements OnInit, OnChanges {

    protected router: Router;
    protected route: ActivatedRoute;
    protected globals: Globals;
    protected commonService: CommonService;
    protected notification: NzNotificationService;
    protected message: NzMessageService;
    protected modalService: NzModalService;

    protected productId: any;

    constructor(private baseInjector: Injector) {
        this.router = this.baseInjector.get(Router);
        this.route = this.baseInjector.get(ActivatedRoute);
        this.globals = this.baseInjector.get(Globals);
        this.commonService = this.baseInjector.get(CommonService);
        this.notification = this.baseInjector.get(NzNotificationService);
        this.message = this.baseInjector.get(NzMessageService);
        this.modalService = this.baseInjector.get(NzModalService);

        let messageDiv = document.getElementsByTagName('nz-message-container');
        if (messageDiv && messageDiv.length > 0) {
            for (let i = 0; i < messageDiv.length; i++) {
                messageDiv[i].parentElement.style.zIndex = '2000'
            }
        }

        let notificationDiv = document.getElementsByTagName('nz-notification-container');
        if (notificationDiv && notificationDiv.length > 0) {
            for (let i = 0; i < notificationDiv.length; i++) {
                notificationDiv[i].parentElement.style.zIndex = '2000'
            }
        }

        this.productId = localStorage.getItem('productId');
    }

    ngOnInit() {
        console.log(124)
    }

    ngOnChanges(changes: SimpleChanges) {
    }

  
    initRouterList(navName: any) {
        if(this.commonService.navList){
            const length = this.commonService.navList.length
            if(length> 0){
                this.commonService.navList[length -1].name = navName;  
            }         
        }
            
    }

    // 生成UUID
    guid() {
        return 'xxxxxxxxxxxx4xxxyxxxxxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
            var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
            return v.toString(16);
        });
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

}
