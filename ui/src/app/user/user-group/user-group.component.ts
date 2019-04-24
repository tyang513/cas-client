import {Component, Injector, ChangeDetectorRef, OnInit, OnChanges} from '@angular/core';
import {UserGroupService} from './user-group.service';
import {BaseComponent} from '../../common/base-component';

@Component({
    selector: 'app-user-group',
    templateUrl: './user-group.component.html',
    styleUrls: ['./user-group.component.less'],
    providers: [UserGroupService]
})
export class UserGroupComponent extends BaseComponent implements OnInit {

    tableList: any = []; // 列表数据

    constructor(public userGroupService: UserGroupService,
                private injector: Injector,
                public changeDetectorRef: ChangeDetectorRef) {
        super(injector);
    }
    ngOnInit() {
        this.advancedSearchList();
    }

    // 高级搜索 返回列表数据
    advancedSearchList() {
        this.userGroupService.getHello().subscribe(response => {
            this.tableList = response;
        });
    }
}
