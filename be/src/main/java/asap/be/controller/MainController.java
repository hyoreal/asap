package asap.be.controller;

import asap.be.dto.CountryDto;
import asap.be.dto.DashboardDto;
import asap.be.dto.EverythingDto;
import asap.be.dto.MoneyDto;
import asap.be.dto.PostProductDto;
import asap.be.dto.EditProductDto;
import asap.be.dto.YearStatusDto;
import asap.be.service.DashBoardService;
import asap.be.service.NotificationService;
import asap.be.service.ProductService;
import asap.be.service.ReleaseService;
import asap.be.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MainController {
	private final ProductService productService;
	private final ReleaseService releaseService;
	private final WarehouseService warehouseService;
	private final DashBoardService dashBoardService;
	private final NotificationService notificationService;

	/**
	 *
	 * 상품의 저장/입고 그리고 출고를 저장하는 컨트롤러
	 * 입고시
	 * @param productDto "pName": "{{$randomWord}}", "price": "{{$randomInt}}", "pCode": "{{$randomUUID}}",
	 *                     "wId": {{$randomInt}}, "pInsert":  {{$randomInt}}
	 * 출고시
	 * @param productDto "pName": "{{$randomWord}}", "price": "{{$randomInt}}", "pCode": "{{$randomUUID}}",
	 *                     "wId": {{$randomInt}}, "quantity": {{$randomInt}}
	 */
	@PostMapping("/prod")
	public ResponseEntity<EverythingDto> addProduct(@RequestBody PostProductDto productDto) {
		productService.insertOrUpdateStock(productDto);
		return new ResponseEntity<>(releaseService.findStockByPNameAndWId(productDto.getPName(), productDto.getWId()), HttpStatus.OK);

	}

	/**
	 * 상품정보 변경
	 * @param dto ("pId": 100003, "sId": 172, "pName": "이름 바꿔잇!")
	 */
	@PatchMapping("/prod")
	public ResponseEntity<EverythingDto> deleteAndUpdateProduct(@RequestBody EditProductDto dto) {

		productService.updateProduct(dto);
		return new ResponseEntity<>(productService.findById(dto.getPId(), dto.getSId()), HttpStatus.OK);
	}

	/**
	 * product의 ID를 통해 최근 21일간 입출고량 컨트롤러
	 * @param pId
	 */
	@GetMapping("/cnt-product-by-date/{p-id}")
	public ResponseEntity<List<DashboardDto.ProductCntDto>> getProductCntByDate(@PathVariable("p-id") Long pId) {

		return new ResponseEntity<>(dashBoardService.CntProduct(pId), HttpStatus.OK);
	}

	/**
	 * 각각의 입고/ 재고 top 10조회
	 */
	@GetMapping("/product-rank")
	public ResponseEntity<DashboardDto.RankDto> getTop10() {

		return new ResponseEntity<>(dashBoardService.ProductCntRank(), HttpStatus.OK);
	}

	/**
	 * 날짜별로 얻어낸 수익 측정 메서드
	 * @param startDate 2023-04-01
	 * @param endDate 2023-04-05
	 */
	@GetMapping("/total-product-amount")
	public ResponseEntity<List<MoneyDto>> getTotalProductAmount(@RequestParam String startDate, @RequestParam String endDate) {

		return new ResponseEntity<>(dashBoardService.TotalProductAmount(startDate, endDate), HttpStatus.OK);
	}

	/**
	 * 특정 년도의 1월~12월까지의 나온 데이터
	 */
	@GetMapping("/monthly-stock-summary")
	public ResponseEntity<List<YearStatusDto>> getMonthlyStockSum(@RequestParam String year) {

		return new ResponseEntity<>(dashBoardService.getMonthlyStockSummary(year), HttpStatus.OK);
	}
	/*
	 * 나라별 상품 포진
	 */
	@GetMapping("/country-product-status")
	public ResponseEntity<List<CountryDto>> getCountryProductStatus() {

		return new ResponseEntity<>(dashBoardService.getCountryProductStatus(), HttpStatus.OK);
	}

	/**
	 * SSE 통신
	 */
	@GetMapping(value = "/connect", produces = "text/event-stream")
	public SseEmitter sseConnection(@RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
		return notificationService.connection(lastEventId);
	}
}
