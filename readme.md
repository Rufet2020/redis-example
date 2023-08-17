<h1 align="center"> Redis-example  </h1> <br>  

## Table of Contents

- [Introduction](#introduction)
- [How to build & How to run](#how-to-build--how-to-run)
- [API](#api)

## Introduction

This repo purposes to show common cases and configurations about Redis & Spring boot. U can find some examples and
generic
structure to work with redisTemplate

## How to build & How to run

1. Clone the repo
   ```sh
   git clone https://github.com/Rufet2020/redis-example.git
   ```

2. Run docker-compose.yaml file located in project structure
   ```sh
   U need to docker installed to ur machine
   ```

3. Open project in your favorite IDEA and run. If you wish you can build and run project via terminal

   3.1. Build project
    ```sh
    $ ./gradlew build
    ```
   3.2. Run project
    ```sh
    $ java -jar app/build/libs/redis-example.jar
    ```

There are used PostgreSql, Redis, by default project running on localhost:8080

# redis-example endpoints

<details>
<summary>Enpoints by controller</summary>

# Order endpoints

| Endpoint                  |   Method   | Description                                             |  
|:--------------------------|:----------:|:--------------------------------------------------------|
| /order/v1/create          |  **POST**  | Create order and cache with annotation                  |
| /order/v2/create          |  **POST**  | Create order and cache on manual config                 |
| /order/v1/:id             |  **GET**   | Get order by id and cache with annotation               |
| /order/v2/:id             |  **GET**   | Get order by id and cache on manual config              |
| /order/all/v1             |  **GET**   | Get all orders and cache with annotation                |
| /order/all/v2             |  **GET**   | Get all orders and cache on manual config               |
| /order/v1/search/:query   |  **GET**   | Search order by any query without any caching process   |
| /order/v2/search/:query   |  **GET**   | Search order by any query with caching result           |
| /order/brand/:brand       |  **GET**   | Get order by brand name and use brand name as cache key |
| /order/brand/:brand/:name |  **GET**   | Get order by brand name & name and use both as key      |
| /order/v1/delete/:id      | **DELETE** | Delete order by id and cache evict with annotation      |
| /order/v2/delete/:id      | **DELETE** | Delete order by id and cache evict with manual config   |

</details>
