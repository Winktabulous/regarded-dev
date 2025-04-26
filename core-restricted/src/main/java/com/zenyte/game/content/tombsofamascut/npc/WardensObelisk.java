package com.zenyte.game.content.tombsofamascut.npc;

import com.near_reality.game.world.entity.TargetSwitchCause;
import com.zenyte.game.content.skills.prayer.Prayer;
import com.zenyte.game.content.tombsofamascut.encounter.WardenEncounter;
import com.zenyte.game.content.tombsofamascut.raid.EncounterStage;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.*;
import com.zenyte.game.world.entity.masks.*;
import com.zenyte.game.world.entity.npc.NPCCombat;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.utils.TimeUnit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Savions
 */
public class WardensObelisk extends TOANPC {

    public static final int ID = 11750;
    private static final int WHEEL_END_GFX_ID = 2234;
    private static final Location[][] WHEEL_LOCATIONS = {
            {new Location(3797, 5154, 1), new Location(3798, 5154, 1), new Location(3799, 5154, 1), new Location(3797, 5155, 1), new Location(3798, 5155, 1), new Location(3799, 5155, 1), new Location(3797, 5156, 1), new Location(3798, 5156, 1), new Location(3799, 5156, 1), new Location(3797, 5157, 1), new Location(3798, 5157, 1), new Location(3805, 5142, 1), new Location(3805, 5143, 1), new Location(3806, 5142, 1), new Location(3807, 5142, 1), new Location(3806, 5143, 1), new Location(3807, 5143, 1), new Location(3805, 5144, 1), new Location(3806, 5144, 1), new Location(3807, 5144, 1), new Location(3806, 5145, 1), new Location(3807, 5145, 1), new Location(3806, 5146, 1), new Location(3807, 5146, 1), new Location(3806, 5147, 1), new Location(3807, 5147, 1), new Location(3807, 5148, 1), new Location(3807, 5149, 1), new Location(3807, 5150, 1), new Location(3800, 5154, 1), new Location(3801, 5154, 1), new Location(3802, 5154, 1), new Location(3800, 5155, 1), new Location(3801, 5155, 1), new Location(3802, 5155, 1), new Location(3803, 5154, 1), new Location(3804, 5154, 1), new Location(3805, 5154, 1), new Location(3803, 5155, 1), new Location(3804, 5155, 1), new Location(3806, 5154, 1), new Location(3800, 5156, 1), new Location(3801, 5156, 1), new Location(3808, 5142, 1), new Location(3808, 5143, 1), new Location(3808, 5144, 1), new Location(3808, 5145, 1), new Location(3808, 5146, 1), new Location(3808, 5147, 1), new Location(3808, 5148, 1), new Location(3808, 5149, 1), new Location(3808, 5150, 1), new Location(3808, 5151, 1), new Location(3808, 5152, 1), new Location(3815, 5152, 1), new Location(3810, 5154, 1), new Location(3811, 5154, 1), new Location(3812, 5153, 1), new Location(3813, 5153, 1), new Location(3814, 5153, 1), new Location(3812, 5154, 1), new Location(3813, 5154, 1), new Location(3814, 5154, 1), new Location(3815, 5153, 1), new Location(3815, 5154, 1), new Location(3808, 5156, 1), new Location(3808, 5157, 1), new Location(3808, 5158, 1), new Location(3809, 5158, 1), new Location(3808, 5159, 1), new Location(3809, 5159, 1), new Location(3808, 5160, 1), new Location(3808, 5161, 1), new Location(3809, 5160, 1), new Location(3809, 5161, 1), new Location(3810, 5161, 1), new Location(3808, 5162, 1), new Location(3808, 5163, 1), new Location(3808, 5164, 1), new Location(3809, 5162, 1), new Location(3810, 5162, 1), new Location(3809, 5163, 1), new Location(3810, 5163, 1), new Location(3809, 5164, 1), new Location(3810, 5164, 1), new Location(3811, 5164, 1), new Location(3808, 5165, 1), new Location(3808, 5166, 1), new Location(3809, 5165, 1), new Location(3810, 5165, 1), new Location(3811, 5165, 1), new Location(3809, 5166, 1), new Location(3810, 5166, 1), new Location(3811, 5166, 1), new Location(3818, 5151, 1), new Location(3819, 5151, 1), new Location(3816, 5152, 1), new Location(3817, 5152, 1), new Location(3818, 5152, 1), new Location(3819, 5152, 1), new Location(3816, 5153, 1), new Location(3817, 5153, 1), new Location(3816, 5154, 1), new Location(3817, 5154, 1), new Location(3818, 5153, 1), new Location(3819, 5153, 1), new Location(3818, 5154, 1), new Location(3819, 5154, 1)},
            {new Location(3797, 5151, 1), new Location(3798, 5151, 1), new Location(3797, 5152, 1), new Location(3798, 5152, 1), new Location(3799, 5152, 1), new Location(3797, 5153, 1), new Location(3798, 5153, 1), new Location(3799, 5153, 1), new Location(3797, 5154, 1), new Location(3798, 5154, 1), new Location(3799, 5154, 1), new Location(3800, 5152, 1), new Location(3801, 5152, 1), new Location(3800, 5153, 1), new Location(3801, 5153, 1), new Location(3802, 5153, 1), new Location(3800, 5154, 1), new Location(3801, 5154, 1), new Location(3802, 5154, 1), new Location(3803, 5153, 1), new Location(3804, 5153, 1), new Location(3803, 5154, 1), new Location(3804, 5154, 1), new Location(3805, 5154, 1), new Location(3806, 5154, 1), new Location(3807, 5158, 1), new Location(3807, 5159, 1), new Location(3807, 5160, 1), new Location(3806, 5161, 1), new Location(3807, 5161, 1), new Location(3805, 5164, 1), new Location(3806, 5162, 1), new Location(3807, 5162, 1), new Location(3806, 5163, 1), new Location(3807, 5163, 1), new Location(3806, 5164, 1), new Location(3807, 5164, 1), new Location(3805, 5165, 1), new Location(3805, 5166, 1), new Location(3806, 5165, 1), new Location(3807, 5165, 1), new Location(3806, 5166, 1), new Location(3807, 5166, 1), new Location(3808, 5142, 1), new Location(3808, 5143, 1), new Location(3809, 5142, 1), new Location(3810, 5142, 1), new Location(3811, 5142, 1), new Location(3809, 5143, 1), new Location(3810, 5143, 1), new Location(3811, 5143, 1), new Location(3808, 5144, 1), new Location(3808, 5145, 1), new Location(3808, 5146, 1), new Location(3809, 5144, 1), new Location(3810, 5144, 1), new Location(3811, 5144, 1), new Location(3809, 5145, 1), new Location(3810, 5145, 1), new Location(3809, 5146, 1), new Location(3810, 5146, 1), new Location(3808, 5147, 1), new Location(3808, 5148, 1), new Location(3808, 5149, 1), new Location(3809, 5147, 1), new Location(3810, 5147, 1), new Location(3809, 5148, 1), new Location(3809, 5149, 1), new Location(3808, 5150, 1), new Location(3808, 5151, 1), new Location(3809, 5150, 1), new Location(3808, 5152, 1), new Location(3810, 5154, 1), new Location(3811, 5154, 1), new Location(3812, 5154, 1), new Location(3813, 5154, 1), new Location(3814, 5154, 1), new Location(3812, 5155, 1), new Location(3813, 5155, 1), new Location(3814, 5155, 1), new Location(3815, 5154, 1), new Location(3815, 5155, 1), new Location(3808, 5156, 1), new Location(3808, 5157, 1), new Location(3808, 5158, 1), new Location(3815, 5156, 1), new Location(3808, 5159, 1), new Location(3808, 5160, 1), new Location(3808, 5161, 1), new Location(3808, 5162, 1), new Location(3808, 5163, 1), new Location(3808, 5164, 1), new Location(3808, 5165, 1), new Location(3808, 5166, 1), new Location(3816, 5154, 1), new Location(3817, 5154, 1), new Location(3816, 5155, 1), new Location(3817, 5155, 1), new Location(3818, 5154, 1), new Location(3819, 5154, 1), new Location(3818, 5155, 1), new Location(3819, 5155, 1), new Location(3816, 5156, 1), new Location(3817, 5156, 1), new Location(3818, 5156, 1), new Location(3819, 5156, 1), new Location(3818, 5157, 1), new Location(3819, 5157, 1)},
            {new Location(3797, 5147, 1), new Location(3798, 5147, 1), new Location(3797, 5148, 1), new Location(3798, 5148, 1), new Location(3799, 5148, 1), new Location(3797, 5149, 1), new Location(3798, 5149, 1), new Location(3799, 5149, 1), new Location(3797, 5150, 1), new Location(3798, 5150, 1), new Location(3799, 5150, 1), new Location(3799, 5151, 1), new Location(3800, 5149, 1), new Location(3801, 5149, 1), new Location(3800, 5150, 1), new Location(3801, 5150, 1), new Location(3802, 5150, 1), new Location(3800, 5151, 1), new Location(3801, 5151, 1), new Location(3802, 5151, 1), new Location(3803, 5151, 1), new Location(3802, 5152, 1), new Location(3803, 5152, 1), new Location(3804, 5152, 1), new Location(3805, 5152, 1), new Location(3805, 5153, 1), new Location(3806, 5153, 1), new Location(3807, 5156, 1), new Location(3806, 5157, 1), new Location(3807, 5157, 1), new Location(3806, 5158, 1), new Location(3805, 5159, 1), new Location(3806, 5159, 1), new Location(3804, 5160, 1), new Location(3805, 5160, 1), new Location(3803, 5161, 1), new Location(3804, 5161, 1), new Location(3805, 5161, 1), new Location(3806, 5160, 1), new Location(3802, 5163, 1), new Location(3801, 5164, 1), new Location(3802, 5164, 1), new Location(3803, 5162, 1), new Location(3804, 5162, 1), new Location(3805, 5162, 1), new Location(3803, 5163, 1), new Location(3804, 5163, 1), new Location(3805, 5163, 1), new Location(3803, 5164, 1), new Location(3804, 5164, 1), new Location(3801, 5165, 1), new Location(3802, 5165, 1), new Location(3800, 5166, 1), new Location(3801, 5166, 1), new Location(3802, 5166, 1), new Location(3803, 5165, 1), new Location(3804, 5165, 1), new Location(3803, 5166, 1), new Location(3804, 5166, 1), new Location(3812, 5142, 1), new Location(3813, 5142, 1), new Location(3814, 5142, 1), new Location(3812, 5143, 1), new Location(3813, 5143, 1), new Location(3814, 5143, 1), new Location(3815, 5142, 1), new Location(3815, 5143, 1), new Location(3811, 5145, 1), new Location(3811, 5146, 1), new Location(3812, 5144, 1), new Location(3813, 5144, 1), new Location(3814, 5144, 1), new Location(3812, 5145, 1), new Location(3813, 5145, 1), new Location(3814, 5145, 1), new Location(3812, 5146, 1), new Location(3813, 5146, 1), new Location(3815, 5144, 1), new Location(3811, 5147, 1), new Location(3810, 5148, 1), new Location(3811, 5148, 1), new Location(3810, 5149, 1), new Location(3811, 5149, 1), new Location(3812, 5147, 1), new Location(3813, 5147, 1), new Location(3812, 5148, 1), new Location(3810, 5150, 1), new Location(3809, 5151, 1), new Location(3810, 5151, 1), new Location(3809, 5152, 1), new Location(3810, 5155, 1), new Location(3811, 5155, 1), new Location(3811, 5156, 1), new Location(3812, 5156, 1), new Location(3813, 5156, 1), new Location(3814, 5156, 1), new Location(3813, 5157, 1), new Location(3814, 5157, 1), new Location(3814, 5158, 1), new Location(3815, 5157, 1), new Location(3815, 5158, 1), new Location(3815, 5159, 1), new Location(3816, 5142, 1), new Location(3816, 5157, 1), new Location(3817, 5157, 1), new Location(3816, 5158, 1), new Location(3817, 5158, 1), new Location(3818, 5158, 1), new Location(3819, 5158, 1), new Location(3816, 5159, 1), new Location(3817, 5159, 1), new Location(3818, 5159, 1), new Location(3819, 5159, 1), new Location(3817, 5160, 1), new Location(3818, 5160, 1), new Location(3819, 5160, 1), new Location(3818, 5161, 1), new Location(3819, 5161, 1)},
            {new Location(3797, 5142, 1), new Location(3798, 5142, 1), new Location(3799, 5142, 1), new Location(3797, 5143, 1), new Location(3798, 5143, 1), new Location(3799, 5143, 1), new Location(3797, 5144, 1), new Location(3798, 5144, 1), new Location(3799, 5144, 1), new Location(3797, 5145, 1), new Location(3798, 5145, 1), new Location(3799, 5145, 1), new Location(3797, 5146, 1), new Location(3798, 5146, 1), new Location(3799, 5146, 1), new Location(3799, 5147, 1), new Location(3799, 5161, 1), new Location(3797, 5162, 1), new Location(3798, 5162, 1), new Location(3799, 5162, 1), new Location(3797, 5163, 1), new Location(3798, 5163, 1), new Location(3799, 5163, 1), new Location(3797, 5164, 1), new Location(3798, 5164, 1), new Location(3799, 5164, 1), new Location(3797, 5165, 1), new Location(3798, 5165, 1), new Location(3799, 5165, 1), new Location(3797, 5166, 1), new Location(3798, 5166, 1), new Location(3799, 5166, 1), new Location(3800, 5143, 1), new Location(3800, 5144, 1), new Location(3800, 5145, 1), new Location(3801, 5145, 1), new Location(3800, 5146, 1), new Location(3801, 5146, 1), new Location(3802, 5146, 1), new Location(3800, 5147, 1), new Location(3801, 5147, 1), new Location(3802, 5147, 1), new Location(3800, 5148, 1), new Location(3801, 5148, 1), new Location(3802, 5148, 1), new Location(3802, 5149, 1), new Location(3803, 5148, 1), new Location(3803, 5149, 1), new Location(3804, 5149, 1), new Location(3803, 5150, 1), new Location(3804, 5150, 1), new Location(3805, 5150, 1), new Location(3804, 5151, 1), new Location(3805, 5151, 1), new Location(3806, 5152, 1), new Location(3804, 5157, 1), new Location(3805, 5157, 1), new Location(3803, 5158, 1), new Location(3804, 5158, 1), new Location(3805, 5158, 1), new Location(3806, 5156, 1), new Location(3802, 5159, 1), new Location(3803, 5159, 1), new Location(3804, 5159, 1), new Location(3800, 5160, 1), new Location(3801, 5160, 1), new Location(3802, 5160, 1), new Location(3800, 5161, 1), new Location(3801, 5161, 1), new Location(3802, 5161, 1), new Location(3803, 5160, 1), new Location(3800, 5162, 1), new Location(3801, 5162, 1), new Location(3802, 5162, 1), new Location(3800, 5163, 1), new Location(3801, 5163, 1), new Location(3800, 5164, 1), new Location(3800, 5165, 1), new Location(3814, 5146, 1), new Location(3815, 5145, 1), new Location(3815, 5146, 1), new Location(3814, 5147, 1), new Location(3813, 5148, 1), new Location(3814, 5148, 1), new Location(3812, 5149, 1), new Location(3813, 5149, 1), new Location(3814, 5149, 1), new Location(3815, 5147, 1), new Location(3815, 5148, 1), new Location(3811, 5150, 1), new Location(3811, 5151, 1), new Location(3812, 5150, 1), new Location(3813, 5150, 1), new Location(3812, 5151, 1), new Location(3810, 5152, 1), new Location(3810, 5156, 1), new Location(3811, 5157, 1), new Location(3811, 5158, 1), new Location(3812, 5157, 1), new Location(3812, 5158, 1), new Location(3813, 5158, 1), new Location(3812, 5159, 1), new Location(3813, 5159, 1), new Location(3814, 5159, 1), new Location(3813, 5160, 1), new Location(3814, 5160, 1), new Location(3814, 5161, 1), new Location(3815, 5160, 1), new Location(3815, 5161, 1), new Location(3814, 5162, 1), new Location(3815, 5162, 1), new Location(3815, 5163, 1), new Location(3817, 5142, 1), new Location(3816, 5143, 1), new Location(3817, 5143, 1), new Location(3818, 5142, 1), new Location(3819, 5142, 1), new Location(3818, 5143, 1), new Location(3819, 5143, 1), new Location(3816, 5144, 1), new Location(3817, 5144, 1), new Location(3816, 5145, 1), new Location(3817, 5145, 1), new Location(3816, 5146, 1), new Location(3817, 5146, 1), new Location(3818, 5144, 1), new Location(3819, 5144, 1), new Location(3818, 5145, 1), new Location(3819, 5145, 1), new Location(3818, 5146, 1), new Location(3819, 5146, 1), new Location(3816, 5147, 1), new Location(3817, 5147, 1), new Location(3816, 5148, 1), new Location(3816, 5160, 1), new Location(3816, 5161, 1), new Location(3817, 5161, 1), new Location(3816, 5162, 1), new Location(3817, 5162, 1), new Location(3816, 5163, 1), new Location(3817, 5163, 1), new Location(3816, 5164, 1), new Location(3817, 5164, 1), new Location(3818, 5162, 1), new Location(3819, 5162, 1), new Location(3818, 5163, 1), new Location(3819, 5163, 1), new Location(3818, 5164, 1), new Location(3819, 5164, 1), new Location(3816, 5165, 1), new Location(3817, 5165, 1), new Location(3817, 5166, 1), new Location(3818, 5165, 1), new Location(3819, 5165, 1), new Location(3818, 5166, 1), new Location(3819, 5166, 1)},
            {new Location(3799, 5157, 1), new Location(3797, 5158, 1), new Location(3798, 5158, 1), new Location(3799, 5158, 1), new Location(3797, 5159, 1), new Location(3798, 5159, 1), new Location(3799, 5159, 1), new Location(3797, 5160, 1), new Location(3798, 5160, 1), new Location(3799, 5160, 1), new Location(3797, 5161, 1), new Location(3798, 5161, 1), new Location(3800, 5142, 1), new Location(3801, 5142, 1), new Location(3802, 5142, 1), new Location(3801, 5143, 1), new Location(3802, 5143, 1), new Location(3803, 5142, 1), new Location(3804, 5142, 1), new Location(3803, 5143, 1), new Location(3804, 5143, 1), new Location(3801, 5144, 1), new Location(3802, 5144, 1), new Location(3802, 5145, 1), new Location(3803, 5144, 1), new Location(3804, 5144, 1), new Location(3803, 5145, 1), new Location(3804, 5145, 1), new Location(3805, 5145, 1), new Location(3803, 5146, 1), new Location(3804, 5146, 1), new Location(3805, 5146, 1), new Location(3803, 5147, 1), new Location(3804, 5147, 1), new Location(3805, 5147, 1), new Location(3804, 5148, 1), new Location(3805, 5148, 1), new Location(3805, 5149, 1), new Location(3806, 5148, 1), new Location(3806, 5149, 1), new Location(3806, 5150, 1), new Location(3806, 5151, 1), new Location(3807, 5151, 1), new Location(3807, 5152, 1), new Location(3805, 5155, 1), new Location(3806, 5155, 1), new Location(3802, 5156, 1), new Location(3800, 5157, 1), new Location(3801, 5157, 1), new Location(3802, 5157, 1), new Location(3800, 5158, 1), new Location(3801, 5158, 1), new Location(3802, 5158, 1), new Location(3803, 5156, 1), new Location(3804, 5156, 1), new Location(3805, 5156, 1), new Location(3803, 5157, 1), new Location(3800, 5159, 1), new Location(3801, 5159, 1), new Location(3815, 5149, 1), new Location(3814, 5150, 1), new Location(3813, 5151, 1), new Location(3814, 5151, 1), new Location(3815, 5150, 1), new Location(3815, 5151, 1), new Location(3811, 5152, 1), new Location(3812, 5152, 1), new Location(3813, 5152, 1), new Location(3814, 5152, 1), new Location(3810, 5153, 1), new Location(3811, 5153, 1), new Location(3809, 5156, 1), new Location(3809, 5157, 1), new Location(3810, 5157, 1), new Location(3810, 5158, 1), new Location(3810, 5159, 1), new Location(3811, 5159, 1), new Location(3810, 5160, 1), new Location(3811, 5160, 1), new Location(3811, 5161, 1), new Location(3812, 5160, 1), new Location(3812, 5161, 1), new Location(3813, 5161, 1), new Location(3811, 5162, 1), new Location(3811, 5163, 1), new Location(3812, 5162, 1), new Location(3813, 5162, 1), new Location(3812, 5163, 1), new Location(3813, 5163, 1), new Location(3814, 5163, 1), new Location(3812, 5164, 1), new Location(3813, 5164, 1), new Location(3814, 5164, 1), new Location(3815, 5164, 1), new Location(3812, 5165, 1), new Location(3813, 5165, 1), new Location(3814, 5165, 1), new Location(3812, 5166, 1), new Location(3813, 5166, 1), new Location(3814, 5166, 1), new Location(3815, 5165, 1), new Location(3815, 5166, 1), new Location(3817, 5148, 1), new Location(3816, 5149, 1), new Location(3817, 5149, 1), new Location(3818, 5147, 1), new Location(3819, 5147, 1), new Location(3818, 5148, 1), new Location(3819, 5148, 1), new Location(3818, 5149, 1), new Location(3819, 5149, 1), new Location(3816, 5150, 1), new Location(3817, 5150, 1), new Location(3816, 5151, 1), new Location(3817, 5151, 1), new Location(3818, 5150, 1), new Location(3819, 5150, 1), new Location(3816, 5166, 1)}
    };
    private static final Location[] INITIAL_BOMB_LOCATIONS = {
            new Location(3816, 5148, 1), new Location(3810, 5161, 1), new Location(3804, 5144, 1),
            new Location(3810, 5153, 1), new Location(3807, 5161, 1), new Location(3818, 5164, 1),
            new Location(3799, 5148, 1), new Location(3798, 5160, 1), new Location(3807, 5147, 1),
            new Location(3811, 5160, 1), new Location(3798, 5154, 1)
    };
    public static final Animation OBELISK_EXPLOSION_ANIMATION = new Animation(9734);
    private static final Animation FULLY_CHARGED_START_ANIMATION = new Animation(9728);
    private static final Animation FULLY_CHARGED_END_ANIMATION = new Animation(9727);
    private static final Animation CANCEL_CHARGE_ANIMATION = new Animation(9729);
    private static final Animation[] CHARGING_ANIMATIONS = {new Animation(9724), new Animation(9725), new Animation(9726)};
    private static final Graphics THUNDER_GFX = new Graphics(2198);
    private static final Graphics WHEEL_START_GFX = new Graphics(2236, 30, 0);
    private static final Graphics SKULL_BOMB_LANDING_GFX = new Graphics(1447);
    private static final Graphics ISOLATION_GROUND_GFX = new Graphics(2235);
    private static final Projectile SKULL_BOMB_PROJECTILE = new Projectile(2225, 100, 3, 23, 40, 97, 64, 0);
    private static final SoundEffect WHEEL_SOUND = new SoundEffect(6092, 15, 60);
    private static final SoundEffect SKULL_BOMB_LANDING_SOUND = new SoundEffect(6260, 9);
    public static final SoundEffect SKULL_BOMB_THUNDER_SOUND = new SoundEffect(6266, 15);
    private static final SoundEffect ISOLATION_SOUND = new SoundEffect(6257);
    private static final SoundEffect[] OBELISK_FULLY_CHARGED_SOUNDS = {new SoundEffect(6182, 9, 30), new SoundEffect(6256, 9, 60),
            new SoundEffect(6234, 9, 90), new SoundEffect(6135, 9, 120)
    };
    private static final SoundEffect[] ISOLATION_AREA_SOUNDS = {new SoundEffect(6174, 10, 30), new SoundEffect(6102, 10, 60),
            new SoundEffect(6079, 10, 90), new SoundEffect(6106, 10, 120), new SoundEffect(6158, 10, 150),
            new SoundEffect(6223, 10, 180), new SoundEffect(6164, 10, 210), new SoundEffect(6147, 10, 240),
            new SoundEffect(6233, 10, 255), new SoundEffect(6186, 10, 255), new SoundEffect(6186, 10, 255),
            new SoundEffect(6186, 10, 255)};
    private final WardenEncounter encounter;
    private final int totalObeliskTicks;
    private final boolean penetration;
    private final List<Location> possibleThunderLocations = new ArrayList<>();
    private int currentObeliskTicks;
    private int thunderTicks;
    private int specialIndex = Utils.random(2);
    private boolean performingSpecial;
    private int wheelIndex = 0;

    public WardensObelisk(Location tile, WardenEncounter encounter, boolean acceleration, boolean penetration) {
        super(ID, tile, Direction.NORTH, 0, encounter);
        this.encounter = encounter;
        this.totalObeliskTicks = acceleration ? 30 : 40;
        this.currentObeliskTicks = totalObeliskTicks;
        this.penetration = penetration;
        final Location middleLocation = getMiddleLocation();
        for (int x = -2; x <= 2; x++) {
            for (int y = -2; y <= 2; y++) {
                if (Math.abs(x) == 2 || Math.abs(y) == 2) {
                   possibleThunderLocations.add(middleLocation.transform(x, y));
                }
            }
        }
        super.hitBar = new EntityHitBar(this) {
            @Override
            public int getType() {
                return 20;
            }
        };
        getUpdateFlags().flag(UpdateFlag.HIT);
        this.combat = new NPCCombat(this) {
            @Override
            public void setTarget(final Entity target, TargetSwitchCause cause) { }
            @Override
            public void forceTarget(final Entity target) { }
        };
    }

    @Override public boolean setHitpoints(int amount) {
        final boolean set = super.setHitpoints(amount);
        if (id == ID + 1 && encounter != null && EncounterStage.STARTED.equals(encounter.getStage())) {
            encounter.getPlayers().forEach(p -> p.getHpHud().updateValue(hitpoints));
        }
        return set;
    }

    @Override
    public void processNPC() {
        super.processNPC();
        if (EncounterStage.STARTED.equals(encounter.getStage()) &&
                encounter.getPhase() == 1 && encounter.isMovingWardenCanAttack()) {
            if (!performingSpecial) {
                if (thunderTicks > 0) {
                    if (--thunderTicks == 0) {
                        specialIndex = (specialIndex + 1) % 3;
                        performingSpecial = true;
                        if (specialIndex == 0) {
                            sendIsolationSpecial(true);
                        } else if (specialIndex == 1) {
                            startBombSpecial();
                        } else if (specialIndex == 2) {
                            startWheelSpecial();
                        }
                    } else {
                        sendThunders();
                    }
                } else if (--currentObeliskTicks <= 0) {
                    setAnimation(FULLY_CHARGED_END_ANIMATION);
                    thunderTicks = 5;
                } else if (currentObeliskTicks == 1) {
                    setAnimation(FULLY_CHARGED_START_ANIMATION);
                    for (SoundEffect sound : OBELISK_FULLY_CHARGED_SOUNDS) {
                        World.sendSoundEffect(getMiddleLocation(), sound);
                    }
                } else {
                    final float percentage = (float) currentObeliskTicks / totalObeliskTicks;
                    if (percentage > .66F) {
                        setAnimation(CHARGING_ANIMATIONS[0]);
                    } else if (percentage > .33F) {
                        setAnimation(CHARGING_ANIMATIONS[1]);
                    } else {
                        setAnimation(CHARGING_ANIMATIONS[2]);
                    }
                }
            }
        }
    }

    private void sendThunders() {
        Collections.shuffle(possibleThunderLocations);
        for (int i = 0; i < Utils.random(3, 5); i++) {
            final Location loc = possibleThunderLocations.get(i);
            World.sendGraphics(THUNDER_GFX, loc);
            for (Player p : encounter.getChallengePlayers()) {
                if (p != null && loc.equals(p.getLocation())) {
                    p.applyHit(new Hit(this, (int) ((penetration ? 14 : 9) * encounter.getParty().getDamageMultiplier()) + Utils.random(3), HitType.DEFAULT));
                    if (penetration) {
                        disableProtectionPrayers(p);
                    }
                }
            }
        }
    }

    private void sendIsolationSpecial(boolean vertical) {
        WorldTasksManager.schedule(encounter.addRunningTask(new WorldTask() {

            private int ticks;
            private int delta = vertical ? 12 : 11;

            @Override
            public void run() {
                if (!EncounterStage.STARTED.equals(encounter.getStage()) || !encounter.isMovingWardenCanAttack() || delta < 0) {
                    resetObelisk();
                    stop();
                    return;
                }
                final Player[] players = encounter.getChallengePlayers();
                if (ticks == 0) {
                    for (Player p : players) {
                        if (p != null) {
                            p.sendSound(ISOLATION_SOUND);
                        }
                    }
                    final Location middleLocation = getMiddleLocation();
                    for (int cd = 2; cd <= delta; cd++) {
                        final Location loc1 = middleLocation.transform(vertical ? 0 : cd, vertical ? cd : 0);
                        final Location loc2 = middleLocation.transform(vertical ? 0 : -cd, vertical ? -cd : 0);
                        World.sendGraphics(new Graphics(WHEEL_END_GFX_ID, cd * 2, 0), loc1);
                        World.sendGraphics(new Graphics(WHEEL_END_GFX_ID, cd * 2, 0), loc2);
                    }
                } else {
                    if (vertical && ticks == 6) {
                        sendIsolationSpecial(false);
                    }
                    final Location middleTile1 = getMiddleLocation().transform(vertical ? 0 : delta, vertical ? delta : 0);
                    final Location middleTile2 = getMiddleLocation().transform(vertical ? 0 : -delta, vertical ? -delta : 0);
                    if (ticks == 1) {
                        for (SoundEffect sound : ISOLATION_AREA_SOUNDS) {
                           World.sendSoundEffect(middleTile1, sound);
                           World.sendSoundEffect(middleTile2, sound);
                        }
                    }
                    final List<Location> tiles = new ArrayList<>();
                    int distance = (vertical ? 12 : 11) - delta;
                    tiles.add(new Location(middleTile1));
                    tiles.add(new Location(middleTile2));
                    while(distance > 0) {
                        if (vertical) {
                            tiles.add(middleTile1.transform(-distance, distance));
                            tiles.add(middleTile1.transform(distance, distance));
                            tiles.add(middleTile2.transform(-distance, -distance));
                            tiles.add(middleTile2.transform(distance, -distance));
                        } else {
                            tiles.add(middleTile1.transform(distance, distance));
                            tiles.add(middleTile1.transform(distance, -distance));
                            tiles.add(middleTile2.transform(-distance, distance));
                            tiles.add(middleTile2.transform(-distance, -distance));
                        }
                        distance--;
                    }
                    tiles.forEach(tile -> {
                        World.sendGraphics(ISOLATION_GROUND_GFX, tile);
                        for (Player p : encounter.getChallengePlayers()) {
                            if (p != null && tile.equals(p.getLocation())) {
                                p.applyHit(new Hit(WardensObelisk.this, (int) ((penetration ? 18 : 13) * encounter.getParty().getDamageMultiplier()) + Utils.random(3), HitType.DEFAULT));
                                if (penetration) {
                                    disableProtectionPrayers(p);
                                }
                            }
                        }
                    });
                    delta--;
                }
                ticks++;
            }
        }), 0, 0);
    }

    private void startBombSpecial() {
        WorldTasksManager.schedule(encounter.addRunningTask(new WorldTask() {
            int count = INITIAL_BOMB_LOCATIONS.length;
            int bombIndex = Utils.random(count - 1);

            @Override
            public void run() {
                if (!EncounterStage.STARTED.equals(encounter.getStage()) || !encounter.isMovingWardenCanAttack() || count-- <= 0) {
                    resetObelisk();
                    stop();
                    return;
                }
                final Location loc = encounter.getLocation(INITIAL_BOMB_LOCATIONS[bombIndex]);
                bombIndex = (bombIndex + 1) % INITIAL_BOMB_LOCATIONS.length;
                World.sendProjectile(WardensObelisk.this, loc, SKULL_BOMB_PROJECTILE);
                World.sendGraphics(SKULL_BOMB_LANDING_GFX, loc);
                World.sendSoundEffect(loc, SKULL_BOMB_LANDING_SOUND);
                WorldTasksManager.schedule(encounter.addRunningTask(new WorldTask() {
                    int ticks = 0;
                    final List<Location> innerTiles = new ArrayList<>();
                    final List<Location> outerTiles = new ArrayList<>();
                    @Override
                    public void run() {
                        if (!EncounterStage.STARTED.equals(encounter.getStage()) || !encounter.isMovingWardenCanAttack() || ticks >= 2) {
                            stop();
                            return;
                        }
                        if (ticks == 0) {
                            World.sendSoundEffect(loc, SKULL_BOMB_THUNDER_SOUND);
                            for (int dx = -3; dx <= 3; dx++) {
                                for (int dy = -3; dy <= 3; dy++) {
                                    if ((dx == 0 && dy != 0 && Math.abs(dy) < 3) | (dy == 0 && dx != 0 && Math.abs(dx) < 3)) {
                                        continue;
                                    }
                                    final Location thunderLoc = loc.transform(dx, dy);
                                    if (Math.abs(dx) == 3 || Math.abs(dy) == 3) {
                                        outerTiles.add(thunderLoc);
                                    } else {
                                        innerTiles.add(thunderLoc);
                                    }
                                }
                            }
                            innerTiles.forEach(l -> World.sendGraphics(new Graphics(THUNDER_GFX.getId(), loc.getTileDistance(l) * 10, 0), l));
                            outerTiles.forEach(l -> World.sendGraphics(new Graphics(THUNDER_GFX.getId(), 30, 0), l));
                        }
                        final List<Location> checkLocations = ticks == 0 ? innerTiles : outerTiles;
                        checkLocations.forEach(l -> {
                            for (Player p : encounter.getChallengePlayers()) {
                                if (p != null && l.equals(p.getLocation())) {
                                    p.applyHit(new Hit(WardensObelisk.this, (int) ((penetration ? 18 : 13) * encounter.getParty().getDamageMultiplier()) + Utils.random(3), HitType.DEFAULT));
                                    if (penetration) {
                                        disableProtectionPrayers(p);
                                    }
                                }
                            }
                        });
                        ticks++;
                    }
                }), 3, 0);
            }
        }), 0, 2);
    }

    private void startWheelSpecial() {
        WorldTasksManager.schedule(encounter.addRunningTask(new WorldTask() {
            int previousWheelIndex = -1;
            int cycles;
            @Override
            public void run() {
                if (!EncounterStage.STARTED.equals(encounter.getStage()) || !encounter.isMovingWardenCanAttack()) {
                    resetObelisk();
                    stop();
                    return;
                }
                if (previousWheelIndex != -1) {
                    for (Location loc : WHEEL_LOCATIONS[previousWheelIndex]) {
                        final Location encounterLoc = encounter.getLocation(loc);
                        World.sendGraphics(new Graphics(WHEEL_END_GFX_ID, 2 * getMiddleLocation().getTileDistance(encounterLoc),0), encounterLoc);
                        for (Player p : encounter.getPlayers()) {
                            if (p != null && p.getLocation().equals(encounterLoc)) {
                                p.applyHit(new Hit(WardensObelisk.this, (int) ((penetration ? 18 : 13) * encounter.getParty().getDamageMultiplier()) + Utils.random(3), HitType.DEFAULT));
                                if (penetration) {
                                    disableProtectionPrayers(p);
                                }
                            }
                        }
                    }
                }
                if (++cycles >= 12) {
                    resetObelisk();
                    stop();
                    return;
                }
                World.sendSoundEffect(getMiddleLocation(), WHEEL_SOUND);
                for (Location loc : WHEEL_LOCATIONS[wheelIndex]) {
                    World.sendGraphics(WHEEL_START_GFX, encounter.getLocation(loc));
                }
                previousWheelIndex = wheelIndex;
                wheelIndex = (wheelIndex + 1) % 5;
            }
        }), 0, 2);
    }

    private void disableProtectionPrayers(final Player player) {
        player.getPrayerManager().deactivatePrayer(Prayer.PROTECT_FROM_MELEE);
        player.getPrayerManager().deactivatePrayer(Prayer.PROTECT_FROM_MAGIC);
        player.getPrayerManager().deactivatePrayer(Prayer.PROTECT_FROM_MISSILES);
        player.sendMessage("<col=ff3045>Your protection prayers have been disabled!</col>");
        player.getTemporaryAttributes().put("prayer delay", System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(5));
    }

    @Override
    public void sendDeath() {
        encounter.startMovingWardenPhase();
        getHitBars().clear();
        getHitBars().add(new RemoveHitBar(hitBar.getType()));
        getUpdateFlags().flag(UpdateFlag.HIT);
    }

    public void resetObelisk() {
        currentObeliskTicks = totalObeliskTicks;
        if (performingSpecial) {
            setAnimation(CANCEL_CHARGE_ANIMATION);
        }
        setAnimation(CHARGING_ANIMATIONS[0]);
        performingSpecial = false;
        thunderTicks = 0;
    }

    @Override
    public float getPointMultiplier() {
        return 1.5F;
    }

    @Override public boolean addWalkStep(int nextX, int nextY, int lastX, int lastY, boolean check) { return false; }

    @Override public void setRespawnTask() {}

    @Override public void setTarget(Entity target, TargetSwitchCause cause) {}

    @Override public void setFaceEntity(Entity entity) {}

    @Override
    public boolean isCycleHealable() { return false; }

    @Override public boolean checkProjectileClip(Player player, boolean melee) {
        return false;
    }

}
