package com.anou.prototype.core.repository

import androidx.lifecycle.Observer
import com.anou.prototype.core.api.ApiService
import com.anou.prototype.core.controller.ApplicationController
import com.anou.prototype.core.db.ApplicationDatabase
import com.anou.prototype.core.db.about.AboutDao
import com.anou.prototype.core.db.about.AboutEntity
import com.anou.prototype.core.service.NetworkConnectivityService
import com.anou.prototype.core.strategy.ResourceWrapper
import com.anou.prototype.core.util.mock
import io.mockk.coEvery
import kotlinx.coroutines.*
import org.assertj.core.api.Assertions
import org.junit.Test
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify


internal class AboutRepositoryTest {
    @Mock
    val applicationController = mock(ApplicationController::class.java)
    @Mock
    val apiService = mock(ApiService::class.java)
    @Mock
    val applicationDatabase = mock(ApplicationDatabase::class.java)
    @Mock
    val aboutDao = mock(AboutDao::class.java)
    @Mock
    val networkService = mock(NetworkConnectivityService::class.java)


    @BeforeEach
    fun setUp() {
    }

    @AfterEach
    fun tearDown() {

    }

    @Test
    fun `test fetching about with network`() {
        val aboutRepository = AboutRepository(applicationDatabase, apiService, networkService)
        val observer = mock<Observer<ResourceWrapper<AboutEntity>>>()
        Mockito.`when`(networkService.getConnectionType()).thenReturn(NetworkConnectivityService.ConnectionType.TYPE_WIFI)
        val deferred = CompletableDeferred<AboutEntity>()
        Mockito.`when`(apiService!!.fetchAbout()).thenReturn(deferred)
        Mockito.`when`(applicationDatabase.aboutDao()).thenReturn(aboutDao)

        runBlocking {
            val aboutEntityFromApi = GlobalScope.launch(newSingleThreadContext("TestGetAboutWithNetwork")) {
                coEvery {
                    aboutRepository.getAbout("test").observeForever(observer)
                    verify(apiService).fetchAbout().await()
                }
            }

            Assertions.assertThat(aboutEntityFromApi).isNotNull

            val aboutEntityFromDB = aboutDao.getAbout("test")
            Assertions.assertThat(aboutEntityFromDB).isNull()

        }
    }

    @Test
    fun `test fetching about without network`() {
                val aboutRepository = AboutRepository(applicationDatabase, apiService, networkService)
        val observer = mock<Observer<ResourceWrapper<AboutEntity>>>()
        Mockito.`when`(networkService.getConnectionType()).thenReturn(NetworkConnectivityService.ConnectionType.TYPE_NO_INTERNET)
        Mockito.`when`(apiService!!.fetchAbout()).thenReturn(null)
        Mockito.`when`(applicationDatabase.aboutDao()).thenReturn(aboutDao)
        Mockito.`when`(aboutDao.getAbout("test")).thenReturn(mock<AboutEntity>())

        runBlocking {
            val aboutEntityFromApi = GlobalScope.launch(newSingleThreadContext("TestGetAboutWithNetwork")) {
                coEvery {
                    aboutRepository.getAbout("test").observeForever(observer)
                    verify(apiService).fetchAbout().await()
                }
            }

            Assertions.assertThat(aboutEntityFromApi).isNotNull

            val aboutEntityFromDB = aboutDao.getAbout("test")
            Assertions.assertThat(aboutEntityFromDB).isNotNull()
        }
    }

    @Test
    fun `test deleting about`() {
        val aboutRepository = AboutRepository(applicationDatabase, apiService, networkService)
        Mockito.`when`(applicationDatabase.aboutDao()).thenReturn(aboutDao)
        Mockito.`when`(aboutDao.deleteByModuleEid("abc")).thenReturn(1)

        aboutRepository.deleteByModuleEid("abc")
        verify(aboutDao).deleteByModuleEid("abc")
    }

}