package com.borisruzanov.russianwives.mvp.model.repository.rating

import com.borisruzanov.russianwives.utils.Consts

object Rating {
    const val ADD_GENDER_RATING = 1
    const val ADD_AGE_RATING = 1
    const val ADD_IMAGE_RATING = 1
    const val ADD_RELATIONSHIP_RATING = 1
    const val ADD_BODY_TYPE_RATING = 1
    const val ADD_ETHNICITY_RATING = 1
    const val ADD_FAITH_RATING = 1
    const val ADD_SMOKE_STATUS_RATING = 1
    const val ADD_DRINK_STATUS_RATING = 1
    const val ADD_HOBBY_RATING = 1
    const val ADD_WILL_OF_KIDS_RATING = 1

}

object Achievements {
    val MUST_INFO_LIST = listOf(Consts.GENDER, Consts.AGE)
    const val MUST_INFO_ACH: String = "must_info"
    const val FULL_PROFILE_ACH = "full_profile"
}
