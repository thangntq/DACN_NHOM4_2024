FROM node:14.20 as builder

WORKDIR /app

COPY package.json .
COPY package-lock.json .
RUN npm install --save --legacy-peer-deps

COPY . .
RUN npm run build -- --configuration production

FROM nginx:1.22
EXPOSE 8001
COPY --from=builder /app/dist/multikart/browser/ /usr/share/nginx/html
COPY nginx.conf /etc/nginx/nginx.conf
CMD ["nginx", "-g", "daemon off;"]