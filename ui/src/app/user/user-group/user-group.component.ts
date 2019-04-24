import {Component, Injector, ChangeDetectorRef, OnInit, OnChanges} from '@angular/core';
import {UserGroupService} from './user-group.service';
import {BaseComponent} from '../../common/base-component';

@Component({
    selector: 'app-user-group',
    templateUrl: './user-group.component.html',
    styleUrls: ['./user-group.component.less'],
    providers: [UserGroupService]
})
export class UserGroupComponent extends BaseComponent implements OnInit, OnChanges {

    tableList: any; // 列表数据

    constructor(public userGroupService: UserGroupService,
                private injector: Injector,
                public changeDetectorRef: ChangeDetectorRef) {
        super(injector);
        const that = this;
    }
    ngOnInit() {
        const that = this;
        that.advancedSearchList();

    }

    // 高级搜索 返回列表数据
    advancedSearchList() {
        const that = this;
        that.userGroupService.getHello().subscribe((response: any) => {
            if (response.code === 200) {
                that.tableList = response.data;
            }else{
                this.message.create('error', response.message);
            }
        });
    }
}
