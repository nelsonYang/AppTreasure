/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * select a.userId, a.payAccount,sum(b.balance) as selfAmount,case when a.recommandAccount = b.userId then sum(b.balance) end as recommandAmount from user a inner join account b  on a.userId = b.userId group by a.userId;
 */
package com.apptreasure.manager.service;

/**
 *
 * @author nelson
 */
public class InquireIntegerationStatService {
    
}
