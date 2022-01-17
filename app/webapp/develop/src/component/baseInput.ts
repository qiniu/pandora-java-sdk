import Store from '@qn-pandora/app-sdk/lib/stores/store'

export interface IOption {
  value: any
  text: string
  row?: IKeyValues
}

interface IProps {
  value: any
  dataset: any
  config: IKeyValues
  options?: IOption[]
  getPopupContainer?: () => HTMLElement
  setConfig: (key: string, value: any) => void
  onChange: (value: any) => void
}

export default abstract class BaseInput extends Store {
  // 用户放置视图的 dom 节点
  element: any

  // 初始化操作
  initialize() {}

  // 视图大小变更时调用
  resize() {}

  // 渲染组件
  abstract render(props: IProps): void

  // 销毁组件时调用，释放资源
  dispose() {}

  constructor(element: any) {
    super()
    this.element = element
  }
}
