(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-9f479afe"],{"02f4":function(e,t,n){var r=n("4588"),o=n("be13");e.exports=function(e){return function(t,n){var i,a,s=String(o(t)),l=r(n),c=s.length;return l<0||l>=c?e?"":void 0:(i=s.charCodeAt(l),i<55296||i>56319||l+1===c||(a=s.charCodeAt(l+1))<56320||a>57343?e?s.charAt(l):i:e?s.slice(l,l+2):a-56320+(i-55296<<10)+65536)}}},"0390":function(e,t,n){"use strict";var r=n("02f4")(!0);e.exports=function(e,t,n){return t+(n?r(e,t).length:1)}},"0bfb":function(e,t,n){"use strict";var r=n("cb7c");e.exports=function(){var e=r(this),t="";return e.global&&(t+="g"),e.ignoreCase&&(t+="i"),e.multiline&&(t+="m"),e.unicode&&(t+="u"),e.sticky&&(t+="y"),t}},"214f":function(e,t,n){"use strict";n("b0c5");var r=n("2aba"),o=n("32e9"),i=n("79e5"),a=n("be13"),s=n("2b4c"),l=n("520a"),c=s("species"),u=!i(function(){var e=/./;return e.exec=function(){var e=[];return e.groups={a:"7"},e},"7"!=="".replace(e,"$<a>")}),f=function(){var e=/(?:)/,t=e.exec;e.exec=function(){return t.apply(this,arguments)};var n="ab".split(e);return 2===n.length&&"a"===n[0]&&"b"===n[1]}();e.exports=function(e,t,n){var d=s(e),v=!i(function(){var t={};return t[d]=function(){return 7},7!=""[e](t)}),p=v?!i(function(){var t=!1,n=/a/;return n.exec=function(){return t=!0,null},"split"===e&&(n.constructor={},n.constructor[c]=function(){return n}),n[d](""),!t}):void 0;if(!v||!p||"replace"===e&&!u||"split"===e&&!f){var g=/./[d],y=n(a,d,""[e],function(e,t,n,r,o){return t.exec===l?v&&!o?{done:!0,value:g.call(t,n,r)}:{done:!0,value:e.call(n,t,r)}:{done:!1}}),h=y[0],m=y[1];r(String.prototype,e,h),o(RegExp.prototype,d,2==t?function(e,t){return m.call(e,this,t)}:function(e){return m.call(e,this)})}}},"28a5":function(e,t,n){"use strict";var r=n("aae3"),o=n("cb7c"),i=n("ebd6"),a=n("0390"),s=n("9def"),l=n("5f1b"),c=n("520a"),u=n("79e5"),f=Math.min,d=[].push,v="split",p="length",g="lastIndex",y=4294967295,h=!u(function(){RegExp(y,"y")});n("214f")("split",2,function(e,t,n,u){var m;return m="c"=="abbc"[v](/(b)*/)[1]||4!="test"[v](/(?:)/,-1)[p]||2!="ab"[v](/(?:ab)*/)[p]||4!="."[v](/(.?)(.?)/)[p]||"."[v](/()()/)[p]>1||""[v](/.?/)[p]?function(e,t){var o=String(this);if(void 0===e&&0===t)return[];if(!r(e))return n.call(o,e,t);var i,a,s,l=[],u=(e.ignoreCase?"i":"")+(e.multiline?"m":"")+(e.unicode?"u":"")+(e.sticky?"y":""),f=0,v=void 0===t?y:t>>>0,h=new RegExp(e.source,u+"g");while(i=c.call(h,o)){if(a=h[g],a>f&&(l.push(o.slice(f,i.index)),i[p]>1&&i.index<o[p]&&d.apply(l,i.slice(1)),s=i[0][p],f=a,l[p]>=v))break;h[g]===i.index&&h[g]++}return f===o[p]?!s&&h.test("")||l.push(""):l.push(o.slice(f)),l[p]>v?l.slice(0,v):l}:"0"[v](void 0,0)[p]?function(e,t){return void 0===e&&0===t?[]:n.call(this,e,t)}:n,[function(n,r){var o=e(this),i=void 0==n?void 0:n[t];return void 0!==i?i.call(n,o,r):m.call(String(o),n,r)},function(e,t){var r=u(m,e,this,t,m!==n);if(r.done)return r.value;var c=o(e),d=String(this),v=i(c,RegExp),p=c.unicode,g=(c.ignoreCase?"i":"")+(c.multiline?"m":"")+(c.unicode?"u":"")+(h?"y":"g"),b=new v(h?c:"^(?:"+c.source+")",g),_=void 0===t?y:t>>>0;if(0===_)return[];if(0===d.length)return null===l(b,d)?[d]:[];var w=0,x=0,I=[];while(x<d.length){b.lastIndex=h?x:0;var C,R=l(b,h?d:d.slice(x));if(null===R||(C=f(s(b.lastIndex+(h?0:x)),d.length))===w)x=a(d,x,p);else{if(I.push(d.slice(w,x)),I.length===_)return I;for(var k=1;k<=R.length-1;k++)if(I.push(R[k]),I.length===_)return I;x=w=C}}return I.push(d.slice(w)),I}]})},"520a":function(e,t,n){"use strict";var r=n("0bfb"),o=RegExp.prototype.exec,i=String.prototype.replace,a=o,s="lastIndex",l=function(){var e=/a/,t=/b*/g;return o.call(e,"a"),o.call(t,"a"),0!==e[s]||0!==t[s]}(),c=void 0!==/()??/.exec("")[1],u=l||c;u&&(a=function(e){var t,n,a,u,f=this;return c&&(n=new RegExp("^"+f.source+"$(?!\\s)",r.call(f))),l&&(t=f[s]),a=o.call(f,e),l&&a&&(f[s]=f.global?a.index+a[0].length:t),c&&a&&a.length>1&&i.call(a[0],n,function(){for(u=1;u<arguments.length-2;u++)void 0===arguments[u]&&(a[u]=void 0)}),a}),e.exports=a},"5f1b":function(e,t,n){"use strict";var r=n("23c6"),o=RegExp.prototype.exec;e.exports=function(e,t){var n=e.exec;if("function"===typeof n){var i=n.call(e,t);if("object"!==typeof i)throw new TypeError("RegExp exec method returned something other than an Object or null");return i}if("RegExp"!==r(e))throw new TypeError("RegExp#exec called on incompatible receiver");return o.call(e,t)}},a5d4:function(e,t,n){"use strict";n.r(t);var r=function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("div",{staticClass:"app-container"},[n("el-form",{staticClass:"demo-form-inline",attrs:{inline:!0,model:e.searchFormData,size:"mini"}},[n("el-form-item",{attrs:{label:"serviceId"}},[n("el-input",{staticStyle:{width:"250px"},attrs:{clearable:!0,placeholder:"serviceId"},model:{value:e.searchFormData.serviceId,callback:function(t){e.$set(e.searchFormData,"serviceId",t)},expression:"searchFormData.serviceId"}})],1),e._v(" "),n("el-form-item",[n("el-button",{attrs:{type:"primary",icon:"el-icon-search"},on:{click:e.onSearchTable}},[e._v("查询")])],1)],1),e._v(" "),n("el-table",{staticStyle:{width:"100%","margin-bottom":"20px"},attrs:{data:e.tableData,border:"","row-key":"id"}},[n("el-table-column",{attrs:{prop:"serviceId",label:"服务名称",width:"200"},scopedSlots:e._u([{key:"default",fn:function(t){return[n("span",{domProps:{innerHTML:e._s(e.renderServiceName(t.row))}})]}}])}),e._v(" "),n("el-table-column",{attrs:{prop:"ipPort",label:"IP端口",width:"250"}}),e._v(" "),n("el-table-column",{attrs:{prop:"metadata",label:"当前环境",width:"100"},scopedSlots:e._u([{key:"default",fn:function(t){return["UP"===t.row.status?n("div",[t.row.parentId>0&&"pre"===t.row.metadata.env?n("el-tag",{attrs:{type:"warning"}},[e._v("预发布")]):e._e(),e._v(" "),t.row.parentId>0&&"gray"===t.row.metadata.env?n("el-tag",{attrs:{type:"info"}},[e._v("灰度")]):e._e(),e._v(" "),t.row.parentId>0&&!t.row.metadata.env?n("el-tag",{attrs:{type:"success"}},[e._v("线上")]):e._e()],1):e._e()]}}])}),e._v(" "),n("el-table-column",{attrs:{prop:"metadata",label:"metadata",width:"250"},scopedSlots:e._u([{key:"default",fn:function(t){return[t.row.parentId>0?n("span",[e._v(e._s(JSON.stringify(t.row.metadata)))]):e._e()]}}])}),e._v(" "),n("el-table-column",{attrs:{prop:"status",label:"服务状态",width:"100"},scopedSlots:e._u([{key:"default",fn:function(t){return[t.row.parentId>0&&"UP"===t.row.status?n("el-tag",{attrs:{type:"success"}},[e._v("正常")]):e._e(),e._v(" "),t.row.parentId>0&&"STARTING"===t.row.status?n("el-tag",{attrs:{type:"info"}},[e._v("正在启动")]):e._e(),e._v(" "),t.row.parentId>0&&"UNKNOWN"===t.row.status?n("el-tag",[e._v("未知")]):e._e(),e._v(" "),t.row.parentId>0&&("OUT_OF_SERVICE"===t.row.status||"DOWN"===t.row.status)?n("el-tag",{attrs:{type:"danger"}},[e._v("已禁用")]):e._e()]}}])}),e._v(" "),n("el-table-column",{attrs:{label:"操作",width:"250"},scopedSlots:e._u([{key:"default",fn:function(t){return["sop-gateway"!==t.row.serviceId.toLowerCase()?n("div",[0===t.row.parentId?n("div",[n("el-button",{attrs:{type:"text",size:"mini"},on:{click:function(n){return e.onGrayConfigUpdate(t.row)}}},[e._v("设置灰度参数")])],1):e._e(),e._v(" "),t.row.parentId>0?n("div",["UP"===t.row.status?n("span",["pre"===t.row.metadata.env?n("el-button",{attrs:{type:"text",size:"mini"},on:{click:function(n){return e.onEnvPreClose(t.row)}}},[e._v("结束预发布")]):e._e(),e._v(" "),"gray"===t.row.metadata.env?n("el-button",{attrs:{type:"text",size:"mini"},on:{click:function(n){return e.onEnvGrayClose(t.row)}}},[e._v("结束灰度")]):e._e(),e._v(" "),t.row.metadata.env?e._e():n("el-button",{attrs:{type:"text",size:"mini"},on:{click:function(n){return e.onEnvPreOpen(t.row)}}},[e._v("开启预发布")]),e._v(" "),t.row.metadata.env?e._e():n("el-button",{attrs:{type:"text",size:"mini"},on:{click:function(n){return e.onEnvGrayOpen(t.row)}}},[e._v("开启灰度")])],1):e._e(),e._v(" "),n("span",{staticStyle:{"margin-left":"10px"}},["UP"===t.row.status?n("el-button",{attrs:{type:"text",size:"mini"},on:{click:function(n){return e.onDisable(t.row)}}},[e._v("禁用")]):e._e(),e._v(" "),"OUT_OF_SERVICE"===t.row.status?n("el-button",{attrs:{type:"text",size:"mini"},on:{click:function(n){return e.onEnable(t.row)}}},[e._v("启用")]):e._e()],1)]):e._e()]):e._e()]}}])})],1),e._v(" "),n("el-dialog",{attrs:{title:"灰度设置",visible:e.grayDialogVisible,"close-on-click-modal":!1},on:{"update:visible":function(t){e.grayDialogVisible=t},close:function(t){return e.resetForm("grayForm")}}},[n("el-form",{ref:"grayForm",attrs:{model:e.grayForm,rules:e.grayFormRules,size:"mini"}},[n("el-form-item",{attrs:{label:"serviceId"}},[e._v("\n        "+e._s(e.grayForm.serviceId)+"\n      ")]),e._v(" "),n("el-tabs",{attrs:{type:"card"},model:{value:e.tabsActiveName,callback:function(t){e.tabsActiveName=t},expression:"tabsActiveName"}},[n("el-tab-pane",{attrs:{label:"灰度用户",name:"first"}},[n("el-alert",{staticStyle:{"margin-bottom":"20px"},attrs:{title:"可以是appId或IP地址，多个用英文逗号隔开",type:"info",closable:!1}}),e._v(" "),n("el-form-item",{attrs:{prop:"userKeyContent"}},[n("el-input",{attrs:{placeholder:"可以是appId或IP地址，多个用英文逗号隔开",type:"textarea",rows:6},model:{value:e.grayForm.userKeyContent,callback:function(t){e.$set(e.grayForm,"userKeyContent",t)},expression:"grayForm.userKeyContent"}})],1)],1),e._v(" "),n("el-tab-pane",{attrs:{label:"接口配置",name:"second"}},[n("el-form-item",[n("el-button",{attrs:{type:"text"},on:{click:e.addNameVersion}},[e._v("新增灰度接口")])],1),e._v(" "),n("table",{attrs:{cellpadding:"0",cellspacing:"0"}},e._l(e.grayForm.grayRouteConfigList,function(t,r){return n("tr",{key:t.key},[n("td",[n("el-form-item",{key:t.key,attrs:{prop:"grayRouteConfigList."+r+".oldRouteId",rules:{required:!0,message:"不能为空",trigger:["blur","change"]}}},[e._v("\n                  老接口：\n                  "),n("el-select",{staticStyle:{"margin-right":"10px"},on:{change:function(n){return e.onChangeOldRoute(t)}},model:{value:t.oldRouteId,callback:function(n){e.$set(t,"oldRouteId",n)},expression:"grayRouteConfig.oldRouteId"}},e._l(e.routeList,function(e){return n("el-option",{key:e.id,attrs:{label:e.name+"("+e.version+")",value:e.id}})}),1)],1)],1),e._v(" "),n("td",[n("el-form-item",{key:t.key+1,attrs:{prop:"grayRouteConfigList."+r+".newVersion",rules:{required:!0,message:"不能为空",trigger:["blur","change"]}}},[e._v("\n                  灰度接口：\n                  "),n("el-select",{attrs:{"no-data-text":"无数据"},model:{value:t.newVersion,callback:function(n){e.$set(t,"newVersion",n)},expression:"grayRouteConfig.newVersion"}},e._l(e.getGraySelectData(t.oldRouteId),function(e){return n("el-option",{key:e.id,attrs:{label:e.name+"("+e.version+")",value:e.version}})}),1)],1)],1),e._v(" "),n("td",{staticStyle:{"vertical-align":"baseline"}},[n("el-button",{directives:[{name:"show",rawName:"v-show",value:e.grayForm.grayRouteConfigList.length>1,expression:"grayForm.grayRouteConfigList.length > 1"}],attrs:{type:"text"},on:{click:function(n){return n.preventDefault(),e.removeNameVersion(t)}}},[e._v("删除")])],1)])}),0)],1)],1)],1),e._v(" "),n("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[n("el-button",{on:{click:function(t){e.grayDialogVisible=!1}}},[e._v("取 消")]),e._v(" "),n("el-button",{attrs:{type:"primary"},on:{click:e.onGrayConfigSave}},[e._v("确 定")])],1)],1)],1)},o=[],i=(n("28a5"),n("ac6a"),n("7f7f"),{data:function(){var e=/^\S+(,\S+)*$/,t=function(t,n,r){""===n?r(new Error("不能为空")):(e.test(n)||r(new Error("格式不正确")),r())};return{searchFormData:{serviceId:""},grayDialogVisible:!1,grayForm:{serviceId:"",userKeyContent:"",onlyUpdateGrayUserkey:!1,grayRouteConfigList:[]},tabsActiveName:"first",routeList:[],selectNameVersion:[],grayFormRules:{userKeyContent:[{required:!0,message:"不能为空",trigger:"blur"},{validator:t,trigger:"blur"}]},tableData:[]}},created:function(){this.loadTable()},methods:{loadTable:function(){this.post("service.instance.list",this.searchFormData,function(e){this.tableData=this.buildTreeData(e.data)})},loadRouteList:function(e){this.post("route.list/1.2",{serviceId:e.toLowerCase()},function(e){this.routeList=e.data})},getGraySelectData:function(e){return this.routeList.filter(function(t){return e!==t.id&&e.indexOf(t.name)>-1})},buildTreeData:function(e){return e.forEach(function(t){var n=t.parentId;0===n||e.forEach(function(e){if(e.id===n){var r=e.children;r||(r=[]),r.push(t),e.children=r}})}),e=e.filter(function(e){return 0===e.parentId}),e},onSearchTable:function(){this.loadTable()},onDisable:function(e){this.confirm("确定要禁用 ".concat(e.serviceId,"(").concat(e.ipPort,") 吗?"),function(t){this.post("service.instance.offline",e,function(){this.tip("禁用成功"),t(),this.loadTableDelay()})})},onEnable:function(e){this.confirm("确定要启用 ".concat(e.serviceId,"(").concat(e.ipPort,") 吗?"),function(t){this.post("service.instance.online",e,function(){this.tip("启用成功"),t(),this.loadTableDelay()})})},doEnvOnline:function(e,t){this.post("service.instance.env.online",e,function(){t&&t.call(this)})},onEnvPreOpen:function(e){this.confirm("确定要开启 ".concat(e.serviceId,"(").concat(e.ipPort,") 预发布吗?"),function(t){this.post("service.instance.env.pre.open",e,function(){this.tip("预发布成功"),t(),this.loadTableDelay()})})},onEnvPreClose:function(e){this.confirm("确定要结束 ".concat(e.serviceId,"(").concat(e.ipPort,") 预发布吗?"),function(t){this.doEnvOnline(e,function(){this.tip("操作成功"),t(),this.loadTableDelay()})})},onEnvGrayOpen:function(e){this.confirm("确定要开启 ".concat(e.serviceId,"(").concat(e.ipPort,") 灰度吗?"),function(t){this.post("service.instance.env.gray.open",e,function(){this.tip("开启成功"),t(),this.loadTableDelay()})})},onEnvGrayClose:function(e){this.confirm("确定要结束 ".concat(e.serviceId,"(").concat(e.ipPort,") 灰度吗?"),function(t){this.doEnvOnline(e,function(){this.tip("操作成功"),t(),this.loadTableDelay()})})},onGrayConfigUpdate:function(e){var t=e.serviceId;this.loadRouteList(t),this.post("service.gray.config.get",{serviceId:t},function(e){var n=this;this.grayDialogVisible=!0,this.$nextTick(function(){var r=e.data;Object.assign(n.grayForm,{serviceId:t,userKeyContent:r.userKeyContent||"",grayRouteConfigList:n.createGrayRouteConfigList(r.nameVersionContent)})})})},onGrayConfigSave:function(){var e=this;this.$refs.grayForm.validate(function(t){if(t){for(var n=[],r=e.grayForm.grayRouteConfigList,o=0;o<r.length;o++){var i=r[o];n.push(i.oldRouteId+"="+i.newVersion)}e.grayForm.nameVersionContent=n.join(","),e.post("service.gray.config.save",e.grayForm,function(){this.grayDialogVisible=!1,this.tip("保存成功")})}})},createGrayRouteConfigList:function(e){if(!e)return[{oldRouteId:"",newVersion:"",key:Date.now()}];for(var t=[],n=e.split(","),r=0;r<n.length;r++){var o=n[r],i=o.split("=");t.push({oldRouteId:i[0],newVersion:i[1],key:Date.now()})}return t},onChangeOldRoute:function(e){e.newVersion=""},addNameVersion:function(){this.grayForm.grayRouteConfigList.push({oldRouteId:"",newVersion:"",key:Date.now()})},removeNameVersion:function(e){var t=this.grayForm.grayRouteConfigList.indexOf(e);-1!==t&&this.grayForm.grayRouteConfigList.splice(t,1)},renderServiceName:function(e){var t="";if(0===e.parentId){var n=e.children||[],r=n.length,o=n.filter(function(e){return"UP"===e.status}).length;t="(".concat(o,"/").concat(r,")")}return e.serviceId+t},loadTableDelay:function(){var e=this;setTimeout(function(){e.loadTable()},2e3)}}}),a=i,s=n("2877"),l=Object(s["a"])(a,r,o,!1,null,null,null);t["default"]=l.exports},b0c5:function(e,t,n){"use strict";var r=n("520a");n("5ca1")({target:"RegExp",proto:!0,forced:r!==/./.exec},{exec:r})}}]);