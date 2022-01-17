type IKeyValues<T = any> = {
  [key: string]: T
}

declare const ResizeObserver: any

declare module '*.less' {
  const style: IKeyValues
  export = style
}

declare module '*.svg' {
  const svgSrc: string
  export default svgSrc
  export const ReactComponent: React.ComponentClass<{ className?: string }>
}

declare module '*.png' {
  const svgSrc: string
  export default svgSrc
}

declare const IS_ENV_PRODUCTION: boolean
declare const USERNAME: string
declare const PASSWORD: string
declare const global: any
