package com.anou.prototype.core.repository

import com.anou.prototype.core.api.ApiService
import com.anou.prototype.core.controller.ApplicationController
import com.anou.prototype.core.db.ApplicationDatabase
import com.anou.prototype.core.db.about.AboutDao
import com.anou.prototype.core.service.NetworkConnectivityService
import org.assertj.core.api.Assertions
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify


internal class AboutRepositoryTest {
    @Mock
    val applicationController = mock(ApplicationController::class.java)
    @Mock
    val rcaApiService = mock(ApiService::class.java)
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
    fun TestGetAboutWithNetwork() {
//        val aboutRepository = AboutRepository(applicationController, applicationDatabase, rcaApiService, networkService)
//        val observer = mock<Observer<ResourceWrapper<AboutEntity>>>()
//        Mockito.`when`(networkService.getConnectionType()).thenReturn(NetworkConnectivityService.ConnectionType.TYPE_WIFI)
//        val deferred = CompletableDeferred<AboutEntity>()
//        Mockito.`when`(rcaApiService!!.getAbout("test")).thenReturn(deferred)
//        Mockito.`when`(applicationDatabase.aboutDao()).thenReturn(aboutDao)
//
//        runBlocking {
//            val aboutEntityFromApi = GlobalScope.launch(newSingleThreadContext("TestGetAboutWithNetwork")) {
//                coEvery {
//                    aboutRepository.getAbout("test").observeForever(observer)
//                    verify(rcaApiService).getAbout("test").await()
//                }
//            }
//
//            Assertions.assertNotNull(aboutEntityFromApi)
//
//            val aboutEntityFromDB = aboutDao.getAbout("test")
//            Assertions.assertNull(aboutEntityFromDB)
//        }
    }

    @Test
    fun TestGetAboutNoNetwork() {
//        val aboutRepository = AboutRepository(applicationController, applicationDatabase, rcaApiService, networkService)
//        val observer = mock<Observer<ResourceWrapper<AboutEntity>>>()
//        Mockito.`when`(networkService.getConnectionType()).thenReturn(NetworkConnectivityService.ConnectionType.TYPE_NO_INTERNET)
//        Mockito.`when`(rcaApiService!!.getAbout("test")).thenReturn(null)
//        Mockito.`when`(applicationDatabase.aboutDao()).thenReturn(aboutDao)
//        Mockito.`when`(aboutDao.getAbout("test")).thenReturn(mock<AboutEntity>())
//
//        runBlocking {
//            val aboutEntityFromApi = GlobalScope.launch(newSingleThreadContext("TestGetAboutWithNetwork")) {
//                coEvery {
//                    aboutRepository.getAbout("test").observeForever(observer)
//                    verify(rcaApiService).getAbout("test").await()
//                }
//            }
//
//            Assertions.assertNull(aboutEntityFromApi)
//
//            val aboutEntityFromDB = aboutDao.getAbout("test")
//            Assertions.assertNotNull(aboutEntityFromDB)
//        }
    }

    @Test
    fun TestDeleteByModuleEid() {
        val aboutRepository = AboutRepository(applicationController, applicationDatabase, rcaApiService, networkService)
        Mockito.`when`(applicationDatabase.aboutDao()).thenReturn(aboutDao)
        Mockito.`when`(aboutDao.deleteByModuleEid("abc")).thenReturn(1)

        aboutRepository.deleteByModuleEid("abc")
        verify(aboutDao).deleteByModuleEid("abc")
    }

    @Test
    fun `test something works as expected`() {
        Assertions.assertTrue(true)
    }

}