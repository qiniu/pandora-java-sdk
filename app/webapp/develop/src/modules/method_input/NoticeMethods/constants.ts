import { ENotifyMode } from '../constants'

export interface IMethodItem {
  active: boolean
  template: string
}

export const defaultMethodItem = {
  active: false,
  template: ''
}

export interface IMethod {
  [ENotifyMode.EMAIL]: IMethodItem
  [ENotifyMode.SMS]: IMethodItem
}

export const defaultMethod: IMethod = {
  [ENotifyMode.EMAIL]: defaultMethodItem,
  [ENotifyMode.SMS]: defaultMethodItem
}
