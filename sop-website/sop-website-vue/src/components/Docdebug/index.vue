<template>
  <div class="doc-debug">
    <h2>{{ docInfo.summary }}</h2>
    <el-table
      :data="[{ methodLabel: '接口名(method)', methodValue: docInfo.name, versionLabel: '版本号(version)', versionValue: docInfo.version }]"
      border
      :cell-style="baseInfoCellStyle"
      :show-header="false"
    >
      <el-table-column prop="methodLabel" align="center" width="130">
        <template slot-scope="scope"><span class="api-info">{{ scope.row.methodLabel }}</span></template>
      </el-table-column>
      <el-table-column prop="methodValue" />
      <el-table-column prop="versionLabel" align="center" width="130">
        <template slot-scope="scope"><span class="api-info">{{ scope.row.versionLabel }}</span></template>
      </el-table-column>
      <el-table-column prop="versionValue" width="120" />
    </el-table>
    <h3>接口描述</h3>
    <div class="doc-overview">{{ docInfo.description || docInfo.title }}</div>
    <h3>请求方法</h3>
    <div class="doc-request-method">
      {{ docInfo.httpMethodList && docInfo.httpMethodList.join(' / ').toUpperCase() }}
    </div>
    <h2>请求参数</h2>
    <parameter-table :data="docInfo.requestParameters" :editable="true" />
    <br/>
    <el-button type="primary" @click="send">发送请求</el-button>
  </div>
</template>
<style>
.api-info {font-weight: bold;}
.doc-overview {margin-top: 20px;margin-bottom: 30px;color: #666;font-size: 14px;}
.doc-request-method {margin-bottom: 20px;color: #666;font-size: 14px;}
.cell .choose-file {padding: 5px;}
.doc-debug .cell .el-form-item {margin-bottom: 0;}
</style>
<script>
import ParameterTable from '@/components/ParameterTable'
export default {
  name: 'Docdebug',
  components: { ParameterTable },
  props: {
    item: {
      type: Object,
      default: () => {}
    },
    uri: {
      type: String,
      default: ''
    },
    urlProd: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      baseInfoCellStyle: (row) => {
        if (row.columnIndex === 0 || row.columnIndex === 2) {
          return { padding: '5px 0', background: '#f5f7fa' }
        } else {
          return { padding: '5px 0' }
        }
      },
      docInfo: {
        summary: '',
        httpMethodList: [],
        requestParameters: [],
        responseParameters: [],
        bizCodes: []
      }
    }
  },
  watch: {
    item(newVal) {
      this.initItem(newVal)
    }
  },
  created() {
  },
  methods: {
    send() {

    },
    initItem(item) {
      this.setData(item)
    },
    setData: function(data) {
      this.docInfo = data
    },
    buildParamData: function(params) {
      const responseJson = {}
      for (let i = 0; i < params.length; i++) {
        const row = params[i]
        if (row.in === 'header') {
          continue
        }
        let val
        // 如果有子节点
        if (row.refs && row.refs.length > 0) {
          const childrenValue = this.buildParamData(row.refs)
          // 如果是数组
          if (row.type === 'array') {
            val = [childrenValue]
          } else {
            val = childrenValue
          }
        } else {
          // 单值
          val = row.paramExample
        }
        responseJson[row.name] = val
      }
      const isOneArray = Object.keys(responseJson).length === 1 && this.isArray(Object.values(responseJson)[0])
      if (isOneArray) {
        return Object.values(responseJson)[0]
      }
      return responseJson
    }
  }
}
</script>
