import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ReactiveFormsModule,FormsModule} from '@angular/forms';
import {NgZorroAntdModule} from 'ng-zorro-antd';
import {RouterModule} from '@angular/router';

import {UserGroupComponent} from './user-group.component';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        NgZorroAntdModule,
        ReactiveFormsModule,
        RouterModule.forChild([{
            path: '',
            component: UserGroupComponent
        }]),
    ],
    declarations: [
        UserGroupComponent,
    ]
})
export class UserGroupModule {
}
