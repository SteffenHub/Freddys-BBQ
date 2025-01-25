export interface Cart {
  items: CartItem[]
  total: number
}

export interface CartItem {
  itemId: string
  itemTitle: string
  itemPrice: number
  count: number
  total: number
}
