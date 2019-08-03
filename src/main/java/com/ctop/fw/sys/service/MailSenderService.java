package com.ctop.fw.sys.service;

import java.io.File;
import java.util.Calendar;
import java.util.List;

import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ctop.base.dto.BaseAttachmentDto;
import com.ctop.base.service.BaseAttachmentService;
import com.ctop.fw.common.constants.Constants.SysEmailStatus;
import com.ctop.fw.common.utils.CommonAssembler;
import com.ctop.fw.sys.dto.SysEmailDto;
import com.ctop.fw.sys.dto.SysEmailInfoDto;
import com.ctop.fw.sys.entity.SysEmail;
import com.ctop.fw.sys.repository.SysEmailRepository;

@Service
@Transactional
public class MailSenderService {
	private static Logger logger = LoggerFactory.getLogger(MailSenderService.class);
    
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private SysEmailService sysEmailService;
    
    @Autowired
    private SysEmailInfoService sysEmailInfoService;
    @Autowired
	private BaseAttachmentService baseAttachmentService;
    @Autowired
	private SysEmailRepository sysEmailRepository; 
    public void send() {
    	SysEmailDto sysEmailDto = null;
    	List<SysEmail> sysEmails=sysEmailRepository.findFirstSendedMailData();
    	if(sysEmails.size()>0){
    		sysEmailDto=CommonAssembler.assemble(sysEmails.get(0), SysEmailDto.class);
    	}
    	SysEmailInfoDto sysEmailInfoDto = null;
    	List<SysEmailInfoDto> emailInfos = null;
    	if (sysEmailDto != null) {
    		emailInfos = sysEmailInfoService.findByEmailUuid(sysEmailDto.getEmailUuid());
    	} else {
    		return;
    	}
    	
    	if (emailInfos.size() > 0) {
    		sysEmailInfoDto = emailInfos.get(0);
    		
    		BaseAttachmentDto attachmentDto = null ;
			if (sysEmailDto.getAttachmentUuid() != null) {
				attachmentDto = baseAttachmentService.getById(sysEmailDto.getAttachmentUuid());
			}
    		//BaseAttachmentDto inlineImageDto = o2oUploadFilesService.getById(sysEmailDto.getInlineImageUuid());
    		MimeMessage mail = javaMailSender.createMimeMessage();
            try {
                MimeMessageHelper message = new MimeMessageHelper(mail, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,"UTF-8");
                message.setTo(sysEmailInfoDto.getReceiverEmail());
              //  message.setFrom(emailFromAddress);
                if (StringUtils.isNotEmpty(sysEmailDto.getTitle())){
                	message.setSubject(sysEmailDto.getTitle());
                }
                message.setText(sysEmailDto.getContent(),true);
				if (attachmentDto != null) {
					File file = new File(attachmentDto.getUrl());
					if (file.exists()){
						message.addAttachment(attachmentDto.getName(), new File(attachmentDto.getUrl()));
					}
				}
				
				/*if (inlineImageDto != null) {
					//TODO暂不实现
				}*/
				
				javaMailSender.send(mail);
	            
	            logger.info("发送数邮件成功;EmailUUid为：" + sysEmailDto.getEmailUuid() + ";EmailInfoUuid为："
						+ sysEmailInfoDto.getEmailInfoUuid());
				 if (emailInfos.size() > 1) {
					 sysEmailDto.setStatus(SysEmailStatus.STATUS_PAR_SENDED);
				 } else {
					 sysEmailDto.setStatus(SysEmailStatus.STATUS_IS_SENDED);
				 }
				 
				 sysEmailInfoDto.setStatus(SysEmailStatus.STATUS_IS_SENDED);
				 sysEmailInfoDto.setSendDate(Calendar.getInstance().getTime());
				// sysEmailInfoDto.setRemark(EmailErrorCodeInof.SEND_SUCCESS_INFO);
				 sysEmailInfoService.updateSysEmailInfo(sysEmailInfoDto);
				 
				 sysEmailDto.setLastSendDate(Calendar.getInstance().getTime());
				 sysEmailService.updateSysEmail(sysEmailDto);
            } catch (Exception e) {
            	e.printStackTrace();
            	sysEmailDto.setStatus(SysEmailStatus.STATUS_FALL_SENDED);
        		sysEmailDto.setExt1("数据错误,未找到邮件内容明细数据!");
        		sysEmailService.updateSysEmail(sysEmailDto);
                
				logger.info("发送数邮件失败;EmailUUid为：" + sysEmailDto.getEmailUuid() + ";EmailInfoUuid为："
						+ sysEmailInfoDto.getEmailInfoUuid());
				 
				 sysEmailInfoDto.setStatus(SysEmailStatus.STATUS_FALL_SENDED);
				 sysEmailInfoDto.setSendDate(Calendar.getInstance().getTime());
				 sysEmailInfoDto.setRemark(e.getMessage());
				 sysEmailInfoService.updateSysEmailInfo(sysEmailInfoDto);
            }
    	} else {
    		sysEmailDto.setStatus(SysEmailStatus.STATUS_FALL_SENDED);
    		sysEmailDto.setExt1("数据错误,未找到邮件内容明细数据!");
    		sysEmailService.updateSysEmail(sysEmailDto);
    		logger.info("emailUuid为:" + sysEmailDto.getEmailUuid() + "的数据错误，没找到邮件内容!");
    	}
    }
}
