import android.content.Context
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import com.snowtouch.barcodetodb.data.Item
import com.snowtouch.barcodetodb.data.OfflineItemsRepository
import com.snowtouch.barcodetodb.ui.AddItemViewModel
import com.snowtouch.barcodetodb.ui.ItemListViewModel
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith
class AddItemViewModelTest {

    private lateinit var viewModel: AddItemViewModel
    private lateinit var offlineItemsRepository: OfflineItemsRepository
    private lateinit var itemListViewModel: ItemListViewModel

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        offlineItemsRepository = mockk(relaxed = true)
        itemListViewModel = mockk(relaxed = true)
        viewModel = AddItemViewModel(offlineItemsRepository, itemListViewModel)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }
    @Test
    fun `resetTextFields resets text fields in itemUiState`() {
        // Given
        val initialState = viewModel.itemUiState
        val updatedState = initialState.copy(
            itemDetails = initialState.itemDetails.copy(
                code = "12345",
                name = "Test Item",
                price = "10.0",
                quantity = "5"
            )
        )
        viewModel.itemUiState = updatedState

        // When
        viewModel.resetTextFields()

        // Then
        val resetState = viewModel.itemUiState
        assert(resetState.itemDetails.code.isEmpty())
        assert(resetState.itemDetails.name.isEmpty())
        assert(resetState.itemDetails.price == "0")
        assert(resetState.itemDetails.quantity == "1")
        // You can add more assertions as needed for other fields
    }
}
