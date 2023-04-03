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

	@PostMapping("/prod")
	public ResponseEntity<EverythingDto> addProduct(@RequestBody PostProductDto productDto) {
		log.info("요청이 왔는지 여부 확인");
		productService.insertOrUpdateStock(productDto);
		return new ResponseEntity<>(releaseService.findStockByPNameAndWId(productDto.getPName(), productDto.getWId()), HttpStatus.OK);

	}

	@PatchMapping("/prod")
	public ResponseEntity<EverythingDto> deleteAndUpdateProduct(@RequestBody EditProductDto dto) {

		productService.updateProduct(dto);
		return new ResponseEntity<>(productService.findById(dto.getPId(), dto.getSId()), HttpStatus.OK);
	}

	@GetMapping("/cnt-product-by-date/{p-id}")
	public ResponseEntity<List<DashboardDto.ProductCntDto>> getProductCntByDate(@PathVariable("p-id") Long pId) {

		return new ResponseEntity<>(dashBoardService.CntProduct(pId), HttpStatus.OK);
	}

	@GetMapping("/product-rank")
	public ResponseEntity<DashboardDto.RankDto> getTop10() {

		return new ResponseEntity<>(dashBoardService.ProductCntRank(), HttpStatus.OK);
	}

	@GetMapping("/total-product-amount")
	public ResponseEntity<List<MoneyDto>> getTotalProductAmount(@RequestParam String startDate, @RequestParam String endDate) {

		return new ResponseEntity<>(dashBoardService.TotalProductAmount(startDate, endDate), HttpStatus.OK);
	}

	@GetMapping("/monthly-stock-summary")
	public ResponseEntity<List<YearStatusDto>> getMonthlyStockSum(@RequestParam String year) {

		return new ResponseEntity<>(dashBoardService.getMonthlyStockSummary(year), HttpStatus.OK);
	}

	@GetMapping("/country-product-status")
	public ResponseEntity<List<CountryDto>> getCountryProductStatus() {

		return new ResponseEntity<>(dashBoardService.getCountryProductStatus(), HttpStatus.OK);
	}

	/**
	 * SSE 통신
	 * sse 통신 위해 MIME 타입은 text/event-stream 로 지정
	 *
	 * lastEventId : 클라이언트 미수신 Event 유실 예방 위함
	 */
	@GetMapping(value = "/connect", produces = "text/event-stream")
	public SseEmitter sseConnection(@RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
		return notificationService.connection(lastEventId);
	}
}
