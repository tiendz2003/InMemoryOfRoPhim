package com.manutd.ronaldo.impl

import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.test.MavericksTestRule
import com.airbnb.mvrx.withState
import com.manutd.ronaldo.domain.GetHomeResourceUseCase
import com.manutd.ronaldo.network.model.Channel
import com.manutd.ronaldo.network.model.ChannelType
import com.manutd.ronaldo.network.model.Group
import com.manutd.ronaldo.network.model.HomeData
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule


class MoviesViewModelTest {

    @get:Rule
    val mavericksTestRule = MavericksTestRule()

    // Mock UseCase
    private val mockUseCase: GetHomeResourceUseCase = mockk()

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        // Khởi tạo ViewModel với State rỗng và UseCase giả
        viewModel = HomeViewModel(
            initialState = MoviesState(),
            getHomeResourceUseCase = mockUseCase
        )
    }

    @Test
    fun `fetchMovies updates state to Success when API returns data`() = runTest {
        // --- GIVEN (Chuẩn bị dữ liệu giả khớp với Domain Model của bạn) ---

        // 1. Tạo Channel giả
        val fakeChannel = Channel(
            id = "channel_1",
            name = "VTV1",
            display = "VTV1 HD",
            description = "News Channel",
            logoUrl = "http://logo.png",
            streamUrl = "http://stream.m3u8",
            shareUrl = "http://share.com"
        )

        // 2. Tạo Group giả (1 Slider, 1 Horizontal)
        val sliderGroup = Group(
            id = "group_1",
            display = "slider",
            name = "Hot Movies",
            channels = listOf(fakeChannel),
            remoteUrl = "http://remote.json",
            type = ChannelType.SLIDER
        )

        val horizontalGroup = Group(
            id = "group_2",
            display = "horizontal",
            name = "Action Movies",
            channels = listOf(fakeChannel),
            remoteUrl = "http://remote.json",
            type = ChannelType.HORIZONTAL
        )

        val fakeGroups = listOf(sliderGroup, horizontalGroup)

        // 3. Tạo HomeData giả (Đây là cái UseCase trả về)
        val fakeHomeData = HomeData(
            id = "home_1",
            name = "Home Page",
            description = "Main Screen",
            color = "#FFFFFF",
            gridNumber = 2,
            image = "http://bg.png",
            groups = fakeGroups // <--- Quan trọng nhất: ViewModel sẽ lấy cái list này
        )

        // Dạy cho Mock: Khi gọi UseCase, trả về fakeHomeData
        // Lưu ý: Nếu hàm invoke có tham số (ví dụ onError), dùng any()
        coEvery { mockUseCase.invoke(any()) } returns fakeHomeData

        // --- WHEN (Hành động) ---
        // Gọi forceRefresh = true để chắc chắn logic chạy
        viewModel.fetchMovies(forceRefresh = true)

        // --- THEN (Kiểm tra) ---
        // 1. Chờ state chuyển sang Success
        viewModel.awaitState().sections is Success

        // 2. Kiểm tra dữ liệu bên trong State
        com.airbnb.mvrx.withState(viewModel) { state ->
            // Kiểm tra trạng thái thành công
            assertTrue(state.sections is Success)

            // Lấy dữ liệu ra
            val groups = state.sections.invoke()

            // Kiểm tra số lượng
            assertEquals(2, groups?.size)

            // Kiểm tra tính chính xác của dữ liệu
            assertEquals("group_1", groups?.get(0)?.id)
            assertEquals(ChannelType.SLIDER, groups?.get(0)?.type)
            assertEquals("group_2", groups?.get(1)?.id)
        }
    }

    @Test
    fun `fetchMovies updates state to Fail when API throws error`() = runTest {
        // --- GIVEN ---
        val errorMessage = "Network Connection Failed"

        // Giả lập UseCase ném ra lỗi (Exception)
        coEvery { mockUseCase.invoke(any()) } throws RuntimeException(errorMessage)

        // --- WHEN ---
        viewModel.fetchMovies(forceRefresh = true)

        // --- THEN ---
        // Chờ state chuyển sang Fail
        viewModel.awaitState().sections is Fail

        withState(viewModel) { state ->
            assertTrue(state.sections is Fail)
            // Kiểm tra message lỗi (nếu ViewModel có cơ chế wrap lỗi)
            // Lưu ý: state.groups là Fail<List<Group>>
            val error = (state.sections as Fail).error
            assertEquals(errorMessage, error.message)
        }
    }
}