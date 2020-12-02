(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-2d0d6219"],{"70f0":function(t,e,o){"use strict";o.r(e);var a=function(){var t=this,e=t.$createElement,o=t._self._c||e;return o("div",{staticClass:"app-container"},[o("el-form",{attrs:{size:"mini"}},[o("el-form-item",[o("el-button",{attrs:{type:"primary",icon:"el-icon-upload"},on:{click:t.onAddSdk}},[t._v("发布SDK")])],1)],1),t._v(" "),o("el-table",{attrs:{data:t.list,border:""}},[o("el-table-column",{attrs:{prop:"name",label:"SDK",width:"120"}}),t._v(" "),o("el-table-column",{attrs:{prop:"version",label:"版本",width:"120"}}),t._v(" "),o("el-table-column",{attrs:{prop:"content",label:"下载地址"},scopedSlots:t._u([{key:"default",fn:function(e){return[o("el-link",{attrs:{type:"primary",href:e.row.content,target:"_blank"}},[t._v(t._s(e.row.content))])]}}])}),t._v(" "),o("el-table-column",{attrs:{label:"操作",width:"150",align:"center"},scopedSlots:t._u([{key:"default",fn:function(e){return[o("el-button",{attrs:{type:"text",size:"mini"},on:{click:function(o){return t.onSdkUpdate(e.row)}}},[t._v("编辑")]),t._v(" "),o("el-button",{attrs:{type:"text",size:"mini"},on:{click:function(o){return t.onSdkDelete(e.row)}}},[t._v("删除")])]}}])})],1),t._v(" "),o("el-dialog",{attrs:{title:t.sdkDlgTitle,visible:t.sdkDlgAddShow,"close-on-click-modal":!1},on:{"update:visible":function(e){t.sdkDlgAddShow=e},close:function(e){return t.resetForm("sdkAddForm")}}},[o("el-form",{ref:"sdkAddForm",attrs:{model:t.sdkFormAddData,rules:t.sdkFormRule,"label-width":"100px"}},[o("el-form-item",{attrs:{prop:"name",label:"选择语言"}},[o("el-select",{attrs:{placeholder:"请选择"},model:{value:t.sdkFormAddData.name,callback:function(e){t.$set(t.sdkFormAddData,"name",e)},expression:"sdkFormAddData.name"}},t._l(t.sdkConfigs,function(t){return o("el-option",{key:t.name,attrs:{label:t.name,value:t.name}})}),1)],1),t._v(" "),o("el-form-item",{attrs:{prop:"version",label:"版本"}},[o("el-input",{attrs:{maxlength:"30","show-word-limit":"",placeholder:"如：1.0"},model:{value:t.sdkFormAddData.version,callback:function(e){t.$set(t.sdkFormAddData,"version",e)},expression:"sdkFormAddData.version"}})],1),t._v(" "),o("el-form-item",{attrs:{prop:"content",label:"下载地址"}},[o("el-input",{attrs:{maxlength:"100","show-word-limit":""},model:{value:t.sdkFormAddData.content,callback:function(e){t.$set(t.sdkFormAddData,"content",e)},expression:"sdkFormAddData.content"}})],1),t._v(" "),o("el-form-item",{attrs:{prop:"extContent",label:"调用示例"}},[o("el-input",{attrs:{type:"textarea",rows:12,placeholder:"填写SDK调用示例代码，支持markdown语法"},model:{value:t.sdkFormAddData.extContent,callback:function(e){t.$set(t.sdkFormAddData,"extContent",e)},expression:"sdkFormAddData.extContent"}})],1)],1),t._v(" "),o("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[o("el-button",{on:{click:function(e){t.sdkDlgAddShow=!1}}},[t._v("取 消")]),t._v(" "),o("el-button",{attrs:{type:"primary"},on:{click:t.onSubmitForm}},[t._v("保 存")])],1)],1)],1)},n=[],d=(o("7f7f"),function(){return{id:0,name:"",version:"",content:"",extContent:""}}),s={data:function(){return{searchFormData:{},sdkDownloadConfig:[],sdkConfigs:[],sdkDlgTitle:"",sdkDlgAddShow:!1,sdkFormUpdateData:d(),sdkFormAddData:d(),sdkFormLoading:!1,sdkFormRule:{name:[{required:!0,message:"请选择语言",trigger:"blur"}],version:[{required:!0,message:"请填版本",trigger:"blur"}],content:[{required:!0,message:"请填写URL",trigger:"blur"}],extContent:[{required:!0,message:"请填写调用示例",trigger:"blur"}]},downloadUrl:"",list:[]}},created:function(){this.loadLangSelector(),this.loadTable()},methods:{loadLangSelector:function(){var t=this;this.getFile("static/sdkConfig.json?q=".concat((new Date).getTime()),function(e){t.sdkConfigs=e.langList})},loadTable:function(){var t=this;this.post("isp.sdk.list",this.searchFormData,function(e){t.list=e.data})},onSizeChange:function(t){this.searchFormData.pageSize=t,this.loadTable()},onPageIndexChange:function(t){this.searchFormData.pageIndex=t,this.loadTable()},onAddSdk:function(){this.sdkDlgTitle="添加SDK",this.sdkFormAddData=d(),this.sdkDlgAddShow=!0},onSdkUpdate:function(t){this.sdkDlgTitle="修改SDK",this.sdkFormAddData=d(),Object.assign(this.sdkFormAddData,t),this.sdkDlgAddShow=!0},onSdkDelete:function(t){var e=this;this.confirm("确认要删除【".concat(t.name,"】吗？"),function(o){e.post("isp.sdk.delete",{id:t.id},function(t){o(),e.tip("删除成功"),e.loadTable()})})},onSubmitForm:function(){var t=this;this.$refs.sdkAddForm.validate(function(e){if(e){var o=t.sdkFormAddData.id?"isp.sdk.update":"isp.sdk.add";t.post(o,t.sdkFormAddData,function(){this.sdkDlgAddShow=!1,this.loadTable()})}})}}},l=s,r=o("2877"),i=Object(r["a"])(l,a,n,!1,null,null,null);e["default"]=i.exports}}]);