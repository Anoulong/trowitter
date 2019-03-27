/*******************************************************************************
 * QuickSeries® Publishing inc.
 *
 *
 * Copyright (c) 1992-2018 QuickSeries® Publishing inc.
 * All rights reserved.
 *
 *
 * This software is the confidential and proprietary information of QuickSeries®
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with QuickSeries® and QuickSeries's Partners.
 *
 *
 * Created by Anou Chanthavong on 2018-03-12.
 */
package com.anou.prototype.core.db.category

import androidx.room.Dao

@Dao
abstract class FaqDao {
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    abstract fun insertAll(faqEntities: List<CategoryEntity>)
//
//    @Query("SELECT * FROM Faq WHERE categoryEid = :categoryEid ORDER BY title_en COLLATE NOCASE DESC")
//    abstract fun loadFaqsByCategoryEidDescending(categoryEid: String): LiveData<List<CategoryEntity>>
//
//    @Query("SELECT * FROM Faq WHERE categoryEid = :categoryEid ORDER BY title_en COLLATE NOCASE ASC")
//    abstract fun loadFaqsByCategoryEidAscending(categoryEid: String): LiveData<List<CategoryEntity>>
//
//    @Query("DELETE FROM Faq WHERE categoryEid = :categoryEid")
//    abstract fun deleteByCategoryId(categoryEid: String)
//
//
//    @Query("SELECT * FROM Faq WHERE categoryEid IN (:categoryEids)")
//    abstract fun loadByCategories(categoryEids: List<String>): LiveData<List<CategoryEntity>>
//
//    @Query("DELETE FROM Faq WHERE categoryEid IN (:categoryEid)")
//    abstract fun deleteByCategories(categoryEid: List<String>)

}
