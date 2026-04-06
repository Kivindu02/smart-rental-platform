import basket_icon_black from './basket_icon_black.png'
import logo from './logo.png'
import heroImage from './heroImage.jpg'
import search_icon from './searchIcon.svg'
import profile_image from './profile_image.png'
import menuIcon from './menuIcon.svg'
import calender_icon from './calenderIcon.svg'

import place_1 from './place_1.jpg'
import place_2 from './place_2.png'
import place_3 from './place_3.jpg'
import place_4 from './place_4.jpg'
import place_5 from './place_5.jpg'
import place_6 from './place_6.jpg'
import place_7 from './place_7.jpg'
import place_8 from './place_8.jpg'
import place_9 from './place_9.jpg'
import place_10 from './place_10.jpg'

import linkedin_icon from './linkendinIcon.svg'
import facebook_icon from './facebookIcon.svg'
import twitter_icon from './twitterIcon.svg'
import cross_icon from './cross_icon.png'
import add_icon from './addIcon.svg'
import order_icon from './order_icon.png'
import rating_starts from './rating_starts.png'
import starIconFilled from './starIconFilled.svg'
import starIconOutlined from './starIconOutlined.svg'
import upload_area from './uploadArea.svg'
import location_icon from './locationIcon.svg'
import dashboard_icon from './dashboardIcon.svg'
import list_icon from './listIcon.svg'

export const assets = {

  logo,
  basket_icon_black,
  heroImage,
  search_icon,
  profile_image,
  linkedin_icon,
  facebook_icon,
  twitter_icon,
  cross_icon,
  add_icon,
  order_icon,
  rating_starts,
  starIconFilled,
  starIconOutlined,
  upload_area,
  menuIcon,
  calender_icon,
  location_icon,
  dashboard_icon,
  list_icon

}

export const types = [
    "House",
    "Room",
    "Commercal Area",
    
];

// Testimonials Dummy Data
export const testimonials = [
    { id: 1, name: "Emma Rodriguez", address: "Barcelona, Spain", image: "https://images.unsplash.com/photo-1633332755192-727a05c4013d?q=80&w=200", rating: 5, review: "I've used many booking platforms before, but none compare to the personalized experience and attention to detail that QuickStay provides." },
    { id: 2, name: "Liam Johnson", address: "New York, USA", image: "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?q=80&w=200", rating: 4, review: "QuickStay exceeded my expectations. The booking process was seamless, and the hotels were absolutely top-notch. Highly recommended!" },
    { id: 3, name: "Sophia Lee", address: "Seoul, South Korea", image: "https://images.unsplash.com/photo-1701615004837-40d8573b6652?q=80&w=200", rating: 5, review: "Amazing service! I always find the best luxury accommodations through QuickStay. Their recommendations never disappoint!" }
];

//user Dummy Data
export const userDummyData = {
  "_id": "user_2unqyL4diJFP1E3pIBnasc7w8hP",
  "username": "kivindu",
  "email": "user.kivindu@gmail.com",
  "image": "https://img.clerk.com/eyJ0eXBlIjoicHJveHkiLCJzcmMiOiJodHRwczovL2ltYWdlcy5jbGVyay5kZXYvdXBsb2FkZWQvaW1nXzJ2N2c5YVpSSEFVYVUxbmVYZ2JkSVVuWnFzWSJ9",
  "role": "Owner",
  "contactno": "0769185629",
  "createdAt": "2025-03-25T09:29:16.367Z",
  "updatedAt": "2025-04-10T06:34:48.719Z",
  "__v": 1,
  "recentSearchedTypes": [
      "Commercial Area"
  ]

}

//Place Dummy Data
export const place_Dummy_list = [
  {
    "_id": "67f7647c197ac559e4089b96",
    "name": "Room available in shared apartment",
    "owner": userDummyData,
    "type": "room",
    "location": "Dehiwala",
    "price": 10000,
    "images": [place_1,place_1,place_1,place_1],
    "isAvailable": true,
    "createdAt": "2025-04-10T06:26:04.013Z",
    "updatedAt": "2025-04-10T06:26:04.013Z",
    "__v": 0

  },
  {
    "_id": "67f76452197ac559e4089b8e",
    "name": "Room available in shared apartment",
    "owner": userDummyData,
    "type": "room",
    "location": "Dehiwala",
    "price": 10000,
    "images": [place_2,place_3,place_4,place_5],
    "isAvailable": true,
    "createdAt": "2025-04-10T06:26:05.013Z",
    "updatedAt": "2025-04-10T06:26:05.013Z",
    "__v": 0

  },
  {
    "_id": "67f76406197ac559e4089b82",
    "name": "Room available in shared apartment",
    "owner": userDummyData,
    "type": "room",
    "location": "Dehiwala",
    "price": 10000,
    "images": [place_3,place_3,place_3,place_3],
    "isAvailable": true,
    "createdAt": "2025-04-10T06:26:06.013Z",
    "updatedAt": "2025-04-10T06:26:06.013Z",
    "__v": 0

  },
  {
    "_id": "67f763d8197ac559e4089b7a",
    "name": "Room available in shared apartment",
    "owner": userDummyData,
    "type": "room",
    "location": "Dehiwala",
    "price": 10000,
    "images": [place_4,place_4,place_4,place_4],
    "isAvailable": true,
    "createdAt": "2025-04-10T06:26:07.013Z",
    "updatedAt": "2025-04-10T06:26:07.013Z",
    "__v": 0

  },
  {
    "_id": "64f7d2e91a2b4a37c01e9f44",
    "name": "Room available in shared apartment",
    "owner": userDummyData,
    "type": "room",
    "location": "Dehiwala",
    "price": 10000,
    "images": [place_5,place_5,place_5,place_5],
    "isAvailable": true,
    "createdAt": "2025-04-10T06:26:08.013Z",
    "updatedAt": "2025-04-10T06:26:08.013Z",
    "__v": 0

  },
  {
    "_id": "64f7d2e91a2b4a37c01e9f45",
    "name": "Room available in shared apartment",
    "owner": userDummyData,
    "type": "room",
    "location": "Dehiwala",
    "price": 10000,
    "images": [place_6,place_6,place_6,place_6],
    "isAvailable": true,
    "createdAt": "2025-04-10T06:26:09.013Z",
    "updatedAt": "2025-04-10T06:26:09.013Z",
    "__v": 0

  },
  {
    "_id": "64f7d2e91a2b4a37c01e9f46",
    "name": "Room available in shared apartment",
    "owner": userDummyData,
    "type": "room",
    "location": "Dehiwala",
    "price": 10000,
    "images": [place_7,place_7,place_7,place_7],
    "isAvailable": true,
    "createdAt": "2025-04-10T06:26:10.013Z",
    "updatedAt": "2025-04-10T06:26:10.013Z",
    "__v": 0

  },
  {
    "_id": "64f7d2e91a2b4a37c01e9f47 ",
    "name": "Room available in shared apartment",
    "owner": userDummyData,
    "type": "room",
    "location": "Dehiwala",
    "price": 10000,
    "images": [place_8,place_8,place_8,place_8],
    "isAvailable": true,
    "createdAt": "2025-04-10T06:26:11.013Z",
    "updatedAt": "2025-04-10T06:26:11.013Z",
    "__v": 0

  },
  {
    "_id": "64f7d2e91a2b4a37c01e9f48",
    "name": "Room available in shared apartment",
    "owner": userDummyData,
    "type": "room",
    "location": "Dehiwala",
    "price": 10000,
    "images": [place_9,place_9,place_9,place_9],
    "isAvailable": true,
    "createdAt": "2025-04-10T06:26:12.013Z",
    "updatedAt": "2025-04-10T06:26:12.013Z",
    "__v": 0

  },
  {
    "_id": "64f7d2e91a2b4a37c01e9f49",
    "name": "Room available in shared apartment",
    "owner": userDummyData,
    "type": "room",
    "location": "Dehiwala",
    "price": 10000,
    "images": [place_10,place_10,place_10,place_10],
    "isAvailable": true,
    "createdAt": "2025-04-10T06:26:13.013Z",
    "updatedAt": "2025-04-10T06:26:13.013Z",
    "__v": 0

  },
]