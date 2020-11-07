<template>
  <div class="app-container">
    <el-container>
      <el-aside width="300px" style="border-right: 1px solid #eee;padding-right: 20px;">
        <el-input
          v-show="docVO.menuProjects.length > 0"
          v-model="filterText"
          prefix-icon="el-icon-search"
          placeholder="搜索：支持接口名，文档标题"
          style="margin-bottom: 10px;"
          size="mini"
          clearable
        />
        <el-tree
          ref="tree"
          :data="docVO.menuProjects"
          :props="defaultProps"
          :filter-node-method="filterNode"
          node-key="id"
          default-expand-all
          highlight-current
          empty-text="暂无文档"
          @current-change="onDocSelect"
        />
      </el-aside>
      <el-main style="padding-top: 0">
        <doc-view
          v-show="item"
          :item="item"
          :url-prod="docVO.urlProd"
          uri="/portal/isv/getDocItem"
        />
      </el-main>
    </el-container>
  </div>
</template>

<script>
import 'mavon-editor/dist/css/index.css'
import docView from '@/components/DocView'

export default {
  components: { docView },
  data() {
    return {
      active: 'info',
      docVO: {
        urlProd: '',
        urlTest: '',
        menuProjects: []
      },
      item: null,
      filterText: '',
      defaultProps: {
        children: 'children',
        label: 'label'
      }
    }
  },
  watch: {
    filterText(val) {
      this.$refs.tree.filter(val)
    }
  },
  created() {
    this.loadTree()
  },
  methods: {
    loadTree: function() {
      this.get('/portal/isv/getDocMenus', {}, function(resp) {
        this.docVO = resp.data
      })
    },
    // 树搜索
    filterNode(value, data) {
      if (!value) return true
      return JSON.stringify(data).indexOf(value) !== -1
    },
    onDocSelect: function(currentNode, beforeNode) {
      this.showDoc(currentNode)
    },
    showDoc: function(node) {
      if (node.id) {
        this.get('/portal/isv/getDocItem', { id: node.id }, function(resp) {
          this.item = resp.data
        })
      }
    }
  }
}
</script>
