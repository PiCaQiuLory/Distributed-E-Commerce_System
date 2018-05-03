package cn.e3mall.common.pojo;

import java.io.Serializable;
import java.util.List;

import com.github.pagehelper.Page;

public class EasyUIDataGridResult implements Serializable{

	private Integer total;

	private List<?> rows;
	
	public EasyUIDataGridResult(){
		
	}

	public EasyUIDataGridResult(Integer total, List<?> rows) {
        this.total = total;
        this.rows = rows;
    }

	public EasyUIDataGridResult(Long total, List<?> rows) {
        this.total = total.intValue();
        this.rows = rows;
    }

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public List<?> getRows() {
		return rows;
	}

	public void setRows(List<?> rows) {
		this.rows = rows;
	}
	
	/**
	 * 构造表格数据
	 * 
	 * @param data
	 * @return
	 */
	public static EasyUIDataGridResult ok(Object data) {
		EasyUIDataGridResult easyUIDataGridResult = new EasyUIDataGridResult();
		if (data instanceof Page<?>) {
			Page<?> page = (Page<?>) data;
			easyUIDataGridResult.setRows(page.getResult());
			easyUIDataGridResult.setTotal((int) page.getTotal());
		} else {
			List<?> list = (List<?>) data;
			easyUIDataGridResult.setRows(list);
			easyUIDataGridResult.setTotal(list.size());
		}
		return easyUIDataGridResult;
	}

}
