package asap.be.repository.mybatis;

import asap.be.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductMapper {
	//TODO:
	// 1. 입고와 출고를 매퍼를 통해 나누기 11
	// 2.출고에서 처음에 cnt가 cnt &gt;= #{p.quantity} 이여부를 판단하고 맞으면 <if>
	void insertOrUpdateStock(@Param("p") PostProductDto dto); // 상품 등록 및 입고

	void insertOrUpdateRelease(@Param("p") PostProductDto dto); // 상품 출고

	void updateProduct(@Param("p") EditProductDto dto);

	EverythingDto findById(@Param("pId") Long pId, @Param("sId") Long sId);

	List<EverythingDto> findByName(String pName);

    List<EverythingPageDto> findByAll(Integer lastId);

    AllProductCntDto findAllCntByPId(Long pId);

    List<DetailInfoDto> detailPageUsingPId(Long pId);

}

