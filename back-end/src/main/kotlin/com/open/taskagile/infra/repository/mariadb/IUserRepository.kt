package com.open.taskagile.infra.repository.mariadb

import com.infobip.spring.data.r2dbc.QuerydslR2dbcRepository
import com.open.taskagile.infra.repository.entity.Users

interface IUserRepository: QuerydslR2dbcRepository<Users, Long>
