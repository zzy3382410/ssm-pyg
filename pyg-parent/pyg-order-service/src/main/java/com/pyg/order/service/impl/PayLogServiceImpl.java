package com.pyg.order.service.impl;
import java.util.List;
import com.pyg.order.service.PayLogService;
import com.pyg.pojo.TbPayLogExample;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pyg.mapper.TbPayLogMapper;
import com.pyg.pojo.TbPayLog;


import entity.PageResult;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class PayLogServiceImpl implements PayLogService {

	@Autowired
	private TbPayLogMapper payLogMapper;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbPayLog> findAll() {
		return payLogMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbPayLog> page=   (Page<TbPayLog>) payLogMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbPayLog payLog) {
		payLogMapper.insert(payLog);		
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbPayLog payLog){
		payLogMapper.updateByPrimaryKey(payLog);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbPayLog findOne(Long id){
		return payLogMapper.selectByPrimaryKey(id+"");
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			payLogMapper.deleteByPrimaryKey(id+"");
		}		
	}
	
	
		@Override
	public PageResult findPage(TbPayLog payLog, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbPayLogExample example=new TbPayLogExample();
		TbPayLogExample.Criteria criteria = example.createCriteria();
		
		if(payLog!=null){			
						if(payLog.getOutTradeNo()!=null && payLog.getOutTradeNo().length()>0){
				criteria.andOutTradeNoLike("%"+payLog.getOutTradeNo()+"%");
			}
			if(payLog.getUserId()!=null && payLog.getUserId().length()>0){
				criteria.andUserIdLike("%"+payLog.getUserId()+"%");
			}
			if(payLog.getTransactionId()!=null && payLog.getTransactionId().length()>0){
				criteria.andTransactionIdLike("%"+payLog.getTransactionId()+"%");
			}
			if(payLog.getTradeState()!=null && payLog.getTradeState().length()>0){
				criteria.andTradeStateLike("%"+payLog.getTradeState()+"%");
			}
			if(payLog.getOrderList()!=null && payLog.getOrderList().length()>0){
				criteria.andOrderListLike("%"+payLog.getOrderList()+"%");
			}
			if(payLog.getPayType()!=null && payLog.getPayType().length()>0){
				criteria.andPayTypeLike("%"+payLog.getPayType()+"%");
			}
	
		}
		
		Page<TbPayLog> page= (Page<TbPayLog>)payLogMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}
	
}
