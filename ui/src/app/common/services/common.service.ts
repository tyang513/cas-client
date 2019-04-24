import {Injectable} from '@angular/core';
import {Router} from '@angular/router';

import {Globals} from '../../utils/globals';

@Injectable()
export class CommonService {

    public navList: any = [];
    public navMap: any = {};
//    public navCurrent: any = {};
    public navHistory: any = null;

    private _productId: any;

    constructor(protected router: Router,
                protected globals: Globals) {
    }

    toDateStr(date: Date) {
        if (date) {
            const dateLong = date.getTime() + 8 * 3600 * 1000;
            return (new Date(dateLong)).toISOString().substr(0, 10);
        } else {
            return date;
        }
    }

    // 跳转到子页面
    goInto(obj: any) {
        this.router.navigate([obj.url, obj.params || {}]);
    }

    // 点击返回的效果，切换到面包屑的上一级
    goBack() {
        history.back();
    }

    goPage(hash: string, hash2?: any) {
        const url = this.globals.getOneUIMenuUrl(hash);
        let menuUrl;
        if (hash2) {
            menuUrl = this.globals.getOneUIMenuUrl(hash2);
        } else {
            menuUrl = url;
        }
        if (window.parent) {
            const response = {
                eventType: 'router',
                eventInfo: {
                    data: url,
                    menuUrl: menuUrl
                }
            };
            window.parent.postMessage(JSON.stringify(response), '*');

            this.navList = null;
        }
    }

    goReportPage(url: string, menuUrl) {
        if (window.parent) {
            const response = {
                eventType: 'router',
                eventInfo: {
                    data: url,
                    menuUrl: menuUrl
                }
            };
            window.parent.postMessage(JSON.stringify(response), '*');
            this.navList = null;
        }
    }

    get productId() {
        let productId: any;
        productId = this._productId;
        if (!productId) {
            if (localStorage.getItem('productId') && localStorage.getItem('productId') !== 'undefined') {
                productId = localStorage.getItem('productId');
            }
        }
        return 1000000;
    }

    public getDateStr(date: any) {
        if (date) {
            const year = date.getFullYear();
            const month = ('0' + (date.getMonth() + 1)).slice(-2);
            const day = ('0' + date.getDate()).slice(-2);
            return year + '-' + month + '-' + day;
        } else {
            return date;
        }
    }

    public getSecondStr(date: any) {
        const hour = ('0' + date.getHours()).slice(-2);
        const min = ('0' + date.getMinutes()).slice(-2);
        const second = ('0' + date.getSeconds()).slice(-2);
        return hour + ':' + min + ':' + second;
    }

}
