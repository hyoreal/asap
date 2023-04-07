package asap.be.repository;

import asap.be.domain.Warehouse;
import asap.be.dto.CountryDto;
import asap.be.dto.DayMaxValueDto;
import asap.be.dto.EverythingDto;

import java.util.List;
import java.util.Map;

public interface WarehouseRepository {
	void wSave(EverythingDto everythingDto);

	void wDelete(Long wId);

	void wChangeName(String newName, String wName);

	void wChangeLoc(String wLoc, String wName);

	List<Warehouse> findWarehouseByName(String wName);

	List<Warehouse> findWarehouseByLoc(String wLoc);
	String findWarehouseLocByWId(Long wId);

	List<CountryDto> countryStatus();

    DayMaxValueDto sixDate(String date);
	Map<String, String> max_receive_item(String date);
	Map<String, String> max_release_item(String date);

	String max_receive_warehouse(String date);
	String max_release_warehouse(String date);
	Integer total_pinsert(String date);
	Integer total_pRelease(String date);

}
