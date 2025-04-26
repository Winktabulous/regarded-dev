package com.near_reality.plugins.item

import com.zenyte.game.content.skills.slayer.BossTask
import com.zenyte.game.content.skills.slayer.RegularTask
import com.zenyte.game.content.skills.slayer.SlayerMaster
import com.zenyte.game.item.Item
import com.zenyte.game.model.item.pluginextensions.ItemPlugin
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.SkillConstants
import com.zenyte.game.world.entity.player.dialogue.Dialogue
import com.zenyte.plugins.dialogue.OptionsMenuD
import java.util.*

@Suppress("unused")
class SlayerTaskPickerScrollOption: ItemPlugin() {

	override fun handle() {
		bind("Redeem") { p: Player, item: Item, _: Int ->
			if (p.slayer.assignment != null) {
				p.sendMessage("You're already on an assignment.")
				return@bind
			}

			val slayerMasters: MutableList<SlayerMaster> = mutableListOf()
			val masterNames: MutableList<String> = mutableListOf()
			for (master in SlayerMaster.values) {
				if (p.skills.combatLevel >= master.combatRequirement && p.skills.getLevelForXp(SkillConstants.SLAYER) >= master.slayerRequirement) {
					slayerMasters.add(master)
					masterNames.add(master.toString())
				}
			}

			p.dialogueManager.start(object : OptionsMenuD(
				p, "Select a Slayer master",
				*masterNames.toTypedArray()
			) {
				override fun handleClick(slotId: Int) {
					if (slotId >= slayerMasters.size) {
						return
					}

					val slayerMaster = slayerMasters[slotId]
					if (slayerMaster == SlayerMaster.SUMONA) {
						val tasks: MutableList<BossTask> = p.slayer.availableBossAssignments
						val names = ArrayList<String>()
						for (task in tasks) {
							names.add(task.taskName)
						}

						p.dialogueManager.start(object :
							OptionsMenuD(p, "Select the task to receive", *names.toTypedArray()) {
							override fun handleClick(slotId: Int) {
								if (slotId >= tasks.size) {
									return
								}

								p.slayer.master = slayerMaster
								var task = tasks[slotId]
								val assignment = when (task) {
									BossTask.TZKAL_ZUK -> player.slayer.getTzKalZukAssignment(slayerMaster)
									BossTask.TZTOK_JAD -> player.slayer.getTzTokJadAssignment(slayerMaster)
									else -> player.slayer.generateSpecificBossTask(slayerMaster, task)
								}
								when(task) {
									BossTask.TZKAL_ZUK, BossTask.TZTOK_JAD -> {
										assignment.amount = 1
										assignment.initialAmount = 1
										p.slayer.assignment = assignment
										p.inventory.deleteItem(item.copy(1))
										p.dialogueManager.start(object :
											Dialogue(p, p.slayer.master.npcId) {
											override fun buildDialogue() {
												npc("Your new task is to kill " + assignment.amount + " " + assignment.task.toString() + ".")
											}
										})
									}
									else -> {
										player.sendInputInt("How many would you like to slay? (3 - 35)") { value: Int ->
											val num = value.coerceAtMost(35).coerceAtLeast(3)
											assignment.amount = num
											assignment.initialAmount = num
											p.slayer.assignment = assignment
											p.inventory.deleteItem(item.copy(1))
											p.dialogueManager.start(object :
												Dialogue(p, p.slayer.master.npcId) {
												override fun buildDialogue() {
													npc("Your new task is to kill " + assignment.amount + " " + assignment.task.toString() + ".")
												}
											})
										}
									}
								}

							}
							override fun cancelOption() = true
						})
					} else {
						val tasks: MutableList<RegularTask> = p.slayer.getAvailableAssignments(slayerMaster)
						val names = ArrayList<String>()
						for (task in tasks) {
							names.add(task.taskName)
						}
						p.dialogueManager.start(object :
							OptionsMenuD(p, "Select the task to receive", *names.toTypedArray()) {
							override fun handleClick(slotId: Int) {
								if (slotId >= tasks.size) {
									return
								}

								p.slayer.master = slayerMaster
								val task = tasks[slotId]
								val assignment = player.slayer.getAssignment(task)
								if (assignment.task is BossTask) {
									player.sendInputInt("How many would you like to slay? (3 - 35)") { value: Int ->
										val num = value.coerceAtMost(35).coerceAtLeast(3)
										assignment.amount = num
										assignment.initialAmount = num
										p.slayer.assignment = assignment
										p.inventory.deleteItem(item.copy(1))
										p.dialogueManager.start(object :
											Dialogue(p, p.slayer.master.npcId) {
											override fun buildDialogue() {
												npc("Your new task is to kill " + assignment.amount + " " + assignment.task.toString() + ".")
											}
										})
									}
								} else {
									p.slayer.assignment = assignment
									p.inventory.deleteItem(item.copy(1))
									p.dialogueManager.start(object :
										Dialogue(p, p.slayer.master.npcId) {
										override fun buildDialogue() {
											npc("Your new task is to kill " + assignment.amount + " " + assignment.task.toString() + ".")
										}
									})
								}
							}

							override fun cancelOption(): Boolean {
								return true
							}
						})
					}
				}

				override fun cancelOption(): Boolean {
					return true
				}
			})
		}
	}

	override fun getItems(): IntArray {
		return intArrayOf(32157)
	}

}