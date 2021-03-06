package com.anou.prototype.core.remote.strategy

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.MediatorLiveData
import com.anou.prototype.core.strategy.RemoteDataFirstStrategy
import com.anou.prototype.core.strategy.ResourceStatus
import com.anou.prototype.core.strategy.ResourceWrapper
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import org.junit.Test

import org.junit.Rule
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.rules.TestRule
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.MockitoAnnotations
import org.mockito.junit.jupiter.MockitoExtension
import java.lang.StringBuilder
import kotlin.reflect.full.isSubclassOf

@ExtendWith(MockitoExtension::class)
class RemoteDataFirstStrategyTest {

    companion object {
        const val TAG_NETWORK = "network"
        const val TAG_NETWORK_ERROR = "network_error"
        const val TAG_NETWORK_UNAVAILABLE = "Remote data is not available"
        const val TAG_LOCAL = "local"
        const val TAG_LOCAL_ERROR = "local_error"
        const val TAG_LOCAL_UNAVAILABLE = "Local data is not available"

        @BeforeAll
        @JvmStatic
        fun initAll() {
            MockitoAnnotations.initMocks(this)
        }

        @AfterAll
        @JvmStatic
        fun tearDownAll() {
        }
    }

    @Rule
    @JvmField
    var rule: TestRule = InstantTaskExecutorRule()

    @Captor
    lateinit var resultCaptor: ArgumentCaptor<ResourceWrapper<String>>
    @Mock
    lateinit var liveData: MediatorLiveData<ResourceWrapper<String>>
    @Mock
    lateinit var storage: StringBuilder
    @Mock
    lateinit var mockLifecycleOwner: LifecycleRegistry

    abstract inner class SutStrategy(liveData: MediatorLiveData<ResourceWrapper<String>>) : RemoteDataFirstStrategy<String>(mainScope = CoroutineScope(Dispatchers.Unconfined), localScope = CoroutineScope(Dispatchers.Unconfined), remoteScope = CoroutineScope(Dispatchers.Unconfined), liveData = liveData)

    @BeforeEach
    fun init() {
    }

    @AfterEach
    fun tearDown() {
    }

    @org.junit.jupiter.api.Test
    fun `test start() - with network value - returns remote wrapped data (happy path)`() {
        // Arrange /////////////////////////////////////////////////////////////////////////////////
        val mockNetworkState = true
        val mockLocalState = true

        // Act /////////////////////////////////////////////////////////////////////////////////////
        mockLifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        val sut = object : SutStrategy(liveData = liveData) {
            override fun isRemoteAvailable(): Boolean = mockNetworkState
            override fun isLocalAvailable(): Boolean = mockLocalState
            override suspend fun fetchData(): Deferred<String> = CompletableDeferred(TAG_NETWORK)
            override suspend fun readData(): Deferred<String> = CompletableDeferred(TAG_LOCAL)
            override suspend fun writeData(data: String) {
                storage.append(data)
            }
        }

        // Assert //////////////////////////////////////////////////////////////////////////////////
        val accessCount = 2
        Mockito.verify(liveData, Mockito.times(accessCount)).postValue(resultCaptor.capture())
        val resultList = resultCaptor.allValues
        assert(resultList.size == accessCount)
        // Resource #1
        assert(resultList[0].value == null)
        assert(resultList[0].status == ResourceStatus.FETCHING)
        assert(resultList[0].error == null)
        assert(!resultList[0].localData)
        assert(resultList[0].warning == null)
        assert(resultList[0].timestamp > 0)
        assert(resultList[0].strategy.isSubclassOf(RemoteDataFirstStrategy::class))
        // Resource #2
        assert(resultList[1].value == TAG_NETWORK)
        assert(resultList[1].status == ResourceStatus.SUCCESS)
        assert(resultList[1].error == null)
        assert(!resultList[1].localData)
        assert(resultList[1].warning == null)
        assert(resultList[1].timestamp > 0)
        assert(resultList[1].strategy.isSubclassOf(RemoteDataFirstStrategy::class))
        // Save
        Mockito.verify(storage, Mockito.times(1)).append(ArgumentMatchers.eq(TAG_NETWORK))
    }

    @org.junit.jupiter.api.Test
    fun `test start() - with network error - returns local wrapped data`() {
        // Arrange /////////////////////////////////////////////////////////////////////////////////
        val mockNetworkState = true
        val mockLocalState = true

        // Act /////////////////////////////////////////////////////////////////////////////////////
        mockLifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        val sut = object : SutStrategy(liveData = liveData) {
            override fun isRemoteAvailable(): Boolean = mockNetworkState
            override fun isLocalAvailable(): Boolean = mockLocalState
            override suspend fun fetchData(): Deferred<String> {
                throw UnsupportedOperationException(TAG_NETWORK_ERROR)
            }

            override suspend fun readData(): Deferred<String> = CompletableDeferred(TAG_LOCAL)
            override suspend fun writeData(data: String) {
                storage.append(data)
            }
        }

        // Assert //////////////////////////////////////////////////////////////////////////////////
        val accessCount = 3
        Mockito.verify(liveData, Mockito.times(accessCount)).postValue(resultCaptor.capture())
        val resultList = resultCaptor.allValues
        assert(resultList.size == accessCount)
        // Resource #1
        assert(resultList[0].value == null)
        assert(resultList[0].status == ResourceStatus.FETCHING)
        assert(resultList[0].error == null)
        assert(!resultList[0].localData)
        assert(resultList[0].warning == null)
        assert(resultList[0].timestamp > 0)
        assert(resultList[0].strategy.isSubclassOf(RemoteDataFirstStrategy::class))
        // Resource #2
        assert(resultList[1].value == null)
        assert(resultList[1].status == ResourceStatus.LOADING)
        assert(resultList[1].error == null)
        assert(resultList[1].localData)
        assert(resultList[1].warning is UnsupportedOperationException && resultList[1].warning?.message == TAG_NETWORK_ERROR)
        assert(resultList[1].timestamp > 0)
        assert(resultList[1].strategy.isSubclassOf(RemoteDataFirstStrategy::class))
        // Resource #3
        assert(resultList[2].value == TAG_LOCAL)
        assert(resultList[2].status == ResourceStatus.SUCCESS)
        assert(resultList[2].error == null)
        assert(resultList[2].localData)
        assert(resultList[2].warning is UnsupportedOperationException && resultList[2].warning?.message == TAG_NETWORK_ERROR)
        assert(resultList[2].timestamp > 0)
        assert(resultList[2].strategy.isSubclassOf(RemoteDataFirstStrategy::class))
        // Save
        Mockito.verify(storage, Mockito.times(0)).append(ArgumentMatchers.eq(TAG_NETWORK))
    }

    @org.junit.jupiter.api.Test
    fun `test start() - with network error and no storage - returns error`() {
        // Arrange /////////////////////////////////////////////////////////////////////////////////
        val mockNetworkState = true
        val mockLocalState = false

        // Act /////////////////////////////////////////////////////////////////////////////////////
        mockLifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        val sut = object : SutStrategy(liveData = liveData) {
            override fun isRemoteAvailable(): Boolean = mockNetworkState
            override fun isLocalAvailable(): Boolean = mockLocalState
            override suspend fun fetchData(): Deferred<String> {
                throw UnsupportedOperationException(TAG_NETWORK_ERROR)
            }

            override suspend fun readData(): Deferred<String> = CompletableDeferred(TAG_LOCAL)
            override suspend fun writeData(data: String) {
                storage.append(data)
            }
        }

        // Assert //////////////////////////////////////////////////////////////////////////////////
        val accessCount = 2
        Mockito.verify(liveData, Mockito.times(accessCount)).postValue(resultCaptor.capture())
        val resultList = resultCaptor.allValues
        assert(resultList.size == accessCount)
        // Resource #1
        assert(resultList[0].value == null)
        assert(resultList[0].status == ResourceStatus.FETCHING)
        assert(resultList[0].error == null)
        assert(!resultList[0].localData)
        assert(resultList[0].warning == null)
        assert(resultList[0].timestamp > 0)
        assert(resultList[0].strategy.isSubclassOf(RemoteDataFirstStrategy::class))
        // Resource #2
        assert(resultList[1].value == null)
        assert(resultList[1].status == ResourceStatus.INVALID)
        assert(resultList[1].error is IllegalStateException && resultList[1].error?.message == TAG_LOCAL_UNAVAILABLE)
        assert(resultList[1].localData)
        assert(resultList[1].warning is UnsupportedOperationException && resultList[1].warning?.message == TAG_NETWORK_ERROR)
        assert(resultList[1].timestamp > 0)
        assert(resultList[1].strategy.isSubclassOf(RemoteDataFirstStrategy::class))
        // Save
        Mockito.verify(storage, Mockito.times(0)).append(ArgumentMatchers.eq(TAG_NETWORK))
    }

    @org.junit.jupiter.api.Test
    fun `test start() - with network error and storage error - returns error`() {
        // Arrange /////////////////////////////////////////////////////////////////////////////////
        val mockNetworkState = true
        val mockLocalState = true

        // Act /////////////////////////////////////////////////////////////////////////////////////
        mockLifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        val sut = object : SutStrategy(liveData = liveData) {
            override fun isRemoteAvailable(): Boolean = mockNetworkState
            override fun isLocalAvailable(): Boolean = mockLocalState
            override suspend fun fetchData(): Deferred<String> {
                throw UnsupportedOperationException(TAG_NETWORK_ERROR)
            }

            override suspend fun readData(): Deferred<String> {
                throw UnsupportedOperationException(TAG_LOCAL_ERROR)
            }

            override suspend fun writeData(data: String) {
                storage.append(data)
            }
        }

        // Assert //////////////////////////////////////////////////////////////////////////////////
        val accessCount = 3
        Mockito.verify(liveData, Mockito.times(accessCount)).postValue(resultCaptor.capture())
        val resultList = resultCaptor.allValues
        assert(resultList.size == accessCount)
        // Resource #1
        assert(resultList[0].value == null)
        assert(resultList[0].status == ResourceStatus.FETCHING)
        assert(resultList[0].error == null)
        assert(!resultList[0].localData)
        assert(resultList[0].warning == null)
        assert(resultList[0].timestamp > 0)
        assert(resultList[0].strategy.isSubclassOf(RemoteDataFirstStrategy::class))
        // Resource #2
        assert(resultList[1].value == null)
        assert(resultList[1].status == ResourceStatus.LOADING)
        assert(resultList[1].error == null)
        assert(resultList[1].localData)
        assert(resultList[1].warning is UnsupportedOperationException && resultList[1].warning?.message == TAG_NETWORK_ERROR)
        assert(resultList[1].timestamp > 0)
        assert(resultList[1].strategy.isSubclassOf(RemoteDataFirstStrategy::class))
        // Resource #3
        assert(resultList[2].value == null)
        assert(resultList[2].status == ResourceStatus.ERROR)
        assert(resultList[2].error is UnsupportedOperationException && resultList[2].error?.message == TAG_LOCAL_ERROR)
        assert(resultList[2].localData)
        assert(resultList[2].warning is UnsupportedOperationException && resultList[2].warning?.message == TAG_NETWORK_ERROR)
        assert(resultList[2].timestamp > 0)
        assert(resultList[2].strategy.isSubclassOf(RemoteDataFirstStrategy::class))
        // Save
        Mockito.verify(storage, Mockito.times(0)).append(ArgumentMatchers.eq(TAG_NETWORK))
    }

    @org.junit.jupiter.api.Test
    fun `test start() - without network connexion - returns local wrapped data`() {
        // Arrange /////////////////////////////////////////////////////////////////////////////////
        val mockNetworkState = false
        val mockLocalState = true

        // Act /////////////////////////////////////////////////////////////////////////////////////
        mockLifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        val sut = object : SutStrategy(liveData = liveData) {
            override fun isRemoteAvailable(): Boolean = mockNetworkState
            override fun isLocalAvailable(): Boolean = mockLocalState
            override suspend fun fetchData(): Deferred<String> = CompletableDeferred(TAG_NETWORK)
            override suspend fun readData(): Deferred<String> = CompletableDeferred(TAG_LOCAL)
            override suspend fun writeData(data: String) {
                storage.append(data)
            }
        }

        // Assert //////////////////////////////////////////////////////////////////////////////////
        val accessCount = 2
        Mockito.verify(liveData, Mockito.times(accessCount)).postValue(resultCaptor.capture())
        val resultList = resultCaptor.allValues
        assert(resultList.size == accessCount)
        // Resource #1
        assert(resultList[0].value == null)
        assert(resultList[0].status == ResourceStatus.LOADING)
        assert(resultList[0].error == null)
        assert(resultList[0].localData)
        assert(resultList[0].warning is IllegalStateException && resultList[0].warning?.message == TAG_NETWORK_UNAVAILABLE)
        assert(resultList[0].timestamp > 0)
        assert(resultList[0].strategy.isSubclassOf(RemoteDataFirstStrategy::class))
        // Resource #2
        assert(resultList[1].value == TAG_LOCAL)
        assert(resultList[1].status == ResourceStatus.SUCCESS)
        assert(resultList[1].error == null)
        assert(resultList[1].localData)
        assert(resultList[1].warning is IllegalStateException && resultList[1].warning?.message == TAG_NETWORK_UNAVAILABLE)
        assert(resultList[1].timestamp > 0)
        assert(resultList[1].strategy.isSubclassOf(RemoteDataFirstStrategy::class))
        // Save
        Mockito.verify(storage, Mockito.times(0)).append(ArgumentMatchers.eq(TAG_NETWORK))
    }

    @org.junit.jupiter.api.Test
    fun `test start() - without network connexion nor storage - returns error`() {
        // Arrange /////////////////////////////////////////////////////////////////////////////////
        val mockNetworkState = false
        val mockLocalState = false

        // Act /////////////////////////////////////////////////////////////////////////////////////
        mockLifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        val sut = object : SutStrategy(liveData = liveData) {
            override fun isRemoteAvailable(): Boolean = mockNetworkState
            override fun isLocalAvailable(): Boolean = mockLocalState
            override suspend fun fetchData(): Deferred<String> = CompletableDeferred(TAG_NETWORK)
            override suspend fun readData(): Deferred<String> = CompletableDeferred(TAG_LOCAL)
            override suspend fun writeData(data: String) {
                storage.append(data)
            }
        }

        // Assert //////////////////////////////////////////////////////////////////////////////////
        val accessCount = 1
        Mockito.verify(liveData, Mockito.times(accessCount)).postValue(resultCaptor.capture())
        val resultList = resultCaptor.allValues
        assert(resultList.size == accessCount)
        // Resource #1
        assert(resultList[0].value == null)
        assert(resultList[0].status == ResourceStatus.INVALID)
        assert(resultList[0].error is IllegalStateException && resultList[0].error?.message == TAG_LOCAL_UNAVAILABLE)
        assert(resultList[0].localData)
        assert(resultList[0].warning is IllegalStateException && resultList[0].warning?.message == TAG_NETWORK_UNAVAILABLE)
        assert(resultList[0].timestamp > 0)
        assert(resultList[0].strategy.isSubclassOf(RemoteDataFirstStrategy::class))
        // Save
        Mockito.verify(storage, Mockito.times(0)).append(ArgumentMatchers.eq(TAG_NETWORK))
    }

    @org.junit.jupiter.api.Test
    fun `test start() - without network connexion and storage error - returns error`() {
        // Arrange /////////////////////////////////////////////////////////////////////////////////
        val mockNetworkState = false
        val mockLocalState = true

        // Act /////////////////////////////////////////////////////////////////////////////////////
        mockLifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        val sut = object : SutStrategy(liveData = liveData) {
            override fun isRemoteAvailable(): Boolean = mockNetworkState
            override fun isLocalAvailable(): Boolean = mockLocalState
            override suspend fun fetchData(): Deferred<String> = CompletableDeferred(TAG_NETWORK)
            override suspend fun readData(): Deferred<String> {
                throw UnsupportedOperationException(TAG_LOCAL_ERROR)
            }

            override suspend fun writeData(data: String) {
                storage.append(data)
            }
        }

        // Assert //////////////////////////////////////////////////////////////////////////////////
        val accessCount = 2
        Mockito.verify(liveData, Mockito.times(accessCount)).postValue(resultCaptor.capture())
        val resultList = resultCaptor.allValues
        assert(resultList.size == accessCount)
        // Resource #1
        assert(resultList[0].value == null)
        assert(resultList[0].status == ResourceStatus.LOADING)
        assert(resultList[0].error == null)
        assert(resultList[0].localData)
        assert(resultList[0].warning is IllegalStateException && resultList[0].warning?.message == TAG_NETWORK_UNAVAILABLE)
        assert(resultList[0].timestamp > 0)
        assert(resultList[0].strategy.isSubclassOf(RemoteDataFirstStrategy::class))
        // Resource #2
        assert(resultList[1].value == null)
        assert(resultList[1].status == ResourceStatus.ERROR)
        assert(resultList[1].error is UnsupportedOperationException && resultList[1].error?.message == TAG_LOCAL_ERROR)
        assert(resultList[1].localData)
        assert(resultList[1].warning is IllegalStateException && resultList[1].warning?.message == TAG_NETWORK_UNAVAILABLE)
        assert(resultList[1].timestamp > 0)
        assert(resultList[1].strategy.isSubclassOf(RemoteDataFirstStrategy::class))
        // Save
        Mockito.verify(storage, Mockito.times(0)).append(ArgumentMatchers.eq(TAG_NETWORK))
    }
}