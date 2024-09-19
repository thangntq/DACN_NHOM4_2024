import { MenuItem } from "./menu.model";

export const MENU: MenuItem[] = [
  {
    id: 1,
    label: "MENUITEMS.MENU.TEXT",
    isTitle: true,
  },
  {
    id: 2,
    label: "MENUITEMS.DASHBOARDS.TEXT",
    icon: "bx-home-circle",
    link: "/dashboard",
  },
  {
    id: 50,
    label: "MENUITEMS.POS.TEXT",
    icon: "mdi mdi-laptop",
    link: "/pos",
  },
  {
    id: 3,
    label: "MENUITEMS.ORDERS.TEXT",
    icon: "bx-cart-alt",
    subItems: [
      {
        id: 4,
        label: "MENUITEMS.ORDERS.LIST.CREATEORDER",
        link: "/orders/createorder",
        parentId: 3,
      },
      {
        id: 5,
        label: "MENUITEMS.ORDERS.LIST.LISTORDERS",
        link: "/orders",
        parentId: 3,
      },
      {
        id: 6,
        label: "MENUITEMS.ORDERS.LIST.EXCHANGE",
        link: "/exchange",
        parentId: 3,
      },
      {
        id: 7,
        label: "MENUITEMS.ORDERS.LIST.WARRANTY",
        link: "/warranty",
        parentId: 3,
      },
    ],
  },
  {
    id: 8,
    label: "MENUITEMS.MANAGERS.TEXT",
    isTitle: true,
  },
  {
    id: 9,
    label: "MENUITEMS.PRODUCTS.TEXT",
    icon: "bx-cube",
    link: "/products",
  },
  {
    id: 10,
    label: "MENUITEMS.CATEGORYS.TEXT",
    icon: "bx-cube",
    link: "/categori",
  },
  {
    id: 25,
    label: "MENUITEMS.CATEGORIES.TEXT",
    icon: "bx-cube",
    link: "/catergoryori",
  },
  {
    id: 19,
    label: "MENUITEMS.DISCOUNTS.TEXT",
    icon: "bx-cube",
    link: "/discounts",
  },
  {
    id: 20,
    label: "MENUITEMS.VOUCHERS.TEXT",
    icon: "bx-cube",
    link: "/vouchers",
  },
  {
    id: 11,
    label: "MENUITEMS.ATTRIBUTES.TEXT",
    icon: "bx-server",
    subItems: [
      {
        id: 12,
        label: "MENUITEMS.ATTRIBUTES.LIST.MATERIAL",
        link: "/attributes/material",
        parentId: 11,
      },
      {
        id: 13,
        label: "MENUITEMS.ATTRIBUTES.LIST.COLOR",
        link: "/attributes/color",
        parentId: 11,
      },
      {
        id: 14,
        label: "MENUITEMS.ATTRIBUTES.LIST.SIZE",
        link: "/attributes/size",
        parentId: 11,
      },
      {
        id: 15,
        label: "MENUITEMS.ATTRIBUTES.LIST.DESIGN",
        link: "/attributes/design",
        parentId: 11,
      },
      {
        id: 16,
        label: "MENUITEMS.ATTRIBUTES.LIST.FORM",
        link: "/attributes/form",
        parentId: 11,
      },
      {
        id: 17,
        label: "MENUITEMS.ATTRIBUTES.LIST.SLEEVE",
        link: "/attributes/sleeve",
        parentId: 11,
      },
      {
        id: 18,
        label: "MENUITEMS.ATTRIBUTES.LIST.COLLAR",
        link: "/attributes/collar",
        parentId: 11,
      },
    ],
  },
  {
    id: 22,
    label: "MENUITEMS.CUSTOMER.TEXT",
    icon: "bx-cube",
    link: "/bxs-customize",
  },
  {
    id: 23,
    label: "MENUITEMS.EMPLOYEE.TEXT",
    icon: "bx-user",
    link: "/employee",
  },
  {
    id: 24,
    label: "MENUITEMS.STATISTICAL.TEXT",
    icon: "bx-pie-chart-alt-2",
    link: "/statistical",
  },
  {
    id: 21,
    label: "MENUITEMS.CONTACT.TEXT",
    icon: "bxs-contact",
    link: "/contact",
  },
];
