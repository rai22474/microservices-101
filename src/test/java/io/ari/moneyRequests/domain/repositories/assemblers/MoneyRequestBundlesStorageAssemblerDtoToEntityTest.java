package io.ari.moneyRequests.domain.repositories.assemblers;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.ari.bucks.domain.Bucks;
import io.ari.bucks.domain.repositories.BucksRepository;
import io.ari.moneyRequests.domain.MoneyRequest;
import io.ari.moneyRequests.domain.MoneyRequestBundle;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest(MoneyRequestBundlesStorageAssembler.class)
public class MoneyRequestBundlesStorageAssemblerDtoToEntityTest {

	@Before
	public void initMocks(){
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void shouldReturnNotNullWhenDeserializingMoneyRequestBundle() {
		MoneyRequestBundle entity = moneyRequestBundlesStorageAssembler.convertDtoToEntity(moneyRequestBundleData);

		assertNotNull("The money request bundle must be not null", entity);
	}

	@Test
	public void shouldGenerateAnEntityWithIdCreationDateBucksAndRequests() {
		MoneyRequestBundle entity = moneyRequestBundlesStorageAssembler.convertDtoToEntity(moneyRequestBundleData);

		assertEquals("The entity must be created with the given bucks.", expectedEntity, entity);
	}

	@Test
	public void shouldGenerateAnEntityWithAReason() {
		MoneyRequestBundle entity = moneyRequestBundlesStorageAssembler.convertDtoToEntity(moneyRequestBundleData);
		verify(entity).setReason(REASON);
	}

	@Test
	public void shouldGenerateAnEntityWithASourceCommand() {
		MoneyRequestBundle entity = moneyRequestBundlesStorageAssembler.convertDtoToEntity(moneyRequestBundleData);
		verify(entity).setSourceCommand(SOURCE_COMMAND);
	}
	
	@Before
	public void prepareExpectedEntity() throws Exception {
		moneyRequests = ImmutableList.of(moneyRequest);
		moneyRequestBundleData = createMoneyRequestBundleData();

		when(moneyRequestsStorageAssembler.convertDtosToDomainEntities(moneyRequestsData)).thenReturn(moneyRequests);

		whenNew(MoneyRequestBundle.class).withArguments(ID, CREATION_DATE, BUCKS_ID, moneyRequest).thenReturn(expectedEntity);
	}

	private Map<String, Object> createMoneyRequestBundleData() {
		moneyRequestsData = ImmutableList.of(ImmutableMap.of("code", "request"));

		Map<String,Object> moneyRequestBundleDtoMap = new HashMap<>();
		
		moneyRequestBundleDtoMap.put("id", ID);
		moneyRequestBundleDtoMap.put("creationDate", CREATION_DATE);
		moneyRequestBundleDtoMap.put("reason", REASON);
		moneyRequestBundleDtoMap.put("agree", BUCKS_ID);
		moneyRequestBundleDtoMap.put("moneyRequests", moneyRequestsData);
		moneyRequestBundleDtoMap.put("sourceCommand",SOURCE_COMMAND);
		
		return ImmutableMap.copyOf(moneyRequestBundleDtoMap);
	}

	private static final String REASON = "some mundane reason";

	private static final String BUCKS_ID = "fd9a6fd79a";

	private static final String ID = "d89fa75ad874a";
	
	private static final String SOURCE_COMMAND = "d89fa75ad874a";

	private static final Date CREATION_DATE = new Date();

	@Mock
	private Bucks bucks;

	@Mock
	private BucksRepository bucksRepository;

	private Map<String, Object> moneyRequestBundleData;

	@InjectMocks
	private MoneyRequestBundlesStorageAssembler moneyRequestBundlesStorageAssembler;

	@Mock
	private MoneyRequestBundle expectedEntity;

	@Mock
	private MoneyRequestsStorageAssembler moneyRequestsStorageAssembler;

	private Collection<Map<String, Object>> moneyRequestsData;

	private Collection<MoneyRequest> moneyRequests;

	@Mock
	private MoneyRequest moneyRequest;

}
