import * as React from 'react'
import { observer } from 'mobx-react'
import { computed } from 'mobx'
import marked from 'marked'
import DOMPurify from 'dompurify'

marked.setOptions({
  breaks: true
})

interface IMarkdownViewerProps {
  text?: string
  className?: string
}

@observer
export default class MarkdownViewer extends React.Component<
  IMarkdownViewerProps,
  any
> {
  @computed
  get markdown() {
    return { __html: DOMPurify.sanitize(marked(this.props.text || '')) }
  }

  render() {
    return (
      <div
        dangerouslySetInnerHTML={this.markdown}
        className={this.props.className}
      />
    )
  }
}
