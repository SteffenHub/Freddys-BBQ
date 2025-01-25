export interface MenuItem {
  id: string,
  price: number,
  title: string,
  imageUrl: string,
  description: string
}

export interface MenuCategory {
  id: string,
  title: string,
  items: MenuItem[],
}

export interface Menu {
  categories: MenuCategory[],
}
