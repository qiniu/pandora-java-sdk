import { utils } from '@qn-pandora/app-sdk'
import { API_PREFIX } from '../constants'
import { ITemplate } from './model'

const {
  fetch,
  url: { formatURL }
} = utils

export function listSmsTemplates(): Promise<{ data: ITemplate[] }> {
  return fetch(`${API_PREFIX}/src_trigger/message/templates/sms`)
}

export function listEmailTemplates(): Promise<{ data: ITemplate[] }> {
  return fetch(`${API_PREFIX}/src_trigger/message/templates/email`)
}

export function getTemplate(id: string): Promise<{ data: ITemplate }> {
  return fetch(
    formatURL(`${API_PREFIX}/src_trigger/message/templates/id`, { id })
  )
}
