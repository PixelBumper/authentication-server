package com.microb.auth.model.repositories

import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.data.repository.Repository
import java.io.Serializable

@NoRepositoryBean
interface BaseRepository<T, ID : Serializable>
   : CrudRepository<T, ID>
