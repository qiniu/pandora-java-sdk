// import { listTemplate, TBriefTemplate } from 'apis/alert/notice/template'
// import ListStore from 'stores/ListStore'
// import { Loadings } from 'services/base/loadings'
// import toasterService from 'services/base/toaster'

// export const loadings = new Loadings()

// export default class NoticeMethodsStore extends ListStore<TBriefTemplate> {
//   @toasterService.handle()
//   @loadings.handle('init')
//   async initData() {
//     const { total, templates } = await listTemplate(this.searchParams)
//     this.setData(templates)
//     this.setTotal(total)
//   }

//   constructor() {
//     super()
//     this.fetchAll = true
//     this.initData()
//   }
// }
