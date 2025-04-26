package com.near_reality.api.util

import com.near_reality.api.model.ApiMemberRank
import com.near_reality.api.model.ApiPrivilege
import com.zenyte.game.world.entity.player.privilege.MemberRank
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege

internal fun ApiMemberRank.toMemberRank() = when(this) {
    ApiMemberRank.NONE -> MemberRank.NONE
    ApiMemberRank.PREMIUM -> MemberRank.PREMIUM
    ApiMemberRank.EXPANSION -> MemberRank.EXPANSION
    ApiMemberRank.EXTREME -> MemberRank.EXTREME
    ApiMemberRank.RESPECTED -> MemberRank.RESPECTED
    ApiMemberRank.LEGENDARY -> MemberRank.LEGENDARY
    ApiMemberRank.MYTHICAL -> MemberRank.MYTHICAL
    ApiMemberRank.UBER -> MemberRank.UBER
    ApiMemberRank.AMASCUT -> MemberRank.AMASCUT
}

internal fun ApiPrivilege.toPrivilege() = when(this) {
    ApiPrivilege.PLAYER -> PlayerPrivilege.PLAYER
    ApiPrivilege.YOUTUBER -> PlayerPrivilege.YOUTUBER
    ApiPrivilege.MEMBER -> PlayerPrivilege.MEMBER
    ApiPrivilege.FORUM_MODERATOR -> PlayerPrivilege.FORUM_MODERATOR
    ApiPrivilege.SUPPORT -> PlayerPrivilege.SUPPORT
    ApiPrivilege.MODERATOR -> PlayerPrivilege.MODERATOR
    ApiPrivilege.SENIOR_MODERATOR -> PlayerPrivilege.SENIOR_MODERATOR
    ApiPrivilege.ADMINISTRATOR -> PlayerPrivilege.ADMINISTRATOR
    ApiPrivilege.DEVELOPER -> PlayerPrivilege.DEVELOPER
    ApiPrivilege.HIDDEN_ADMINISTRATOR -> PlayerPrivilege.HIDDEN_ADMINISTRATOR
    ApiPrivilege.TRUE_DEVELOPER -> PlayerPrivilege.TRUE_DEVELOPER
}
