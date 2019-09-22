package org.goblinframework.monitor.instruction

import org.goblinframework.core.mbean.GoblinManagedBean
import org.goblinframework.core.mbean.GoblinManagedObject
import org.goblinframework.core.monitor.Instruction
import org.goblinframework.core.monitor.InstructionTranslator

@GoblinManagedBean("monitor", "InstructionTranslator")
class ManagedInstructionTranslator<E : Instruction>
internal constructor(private val delegator: InstructionTranslator<E>)
  : GoblinManagedObject(), InstructionTranslatorMXBean, InstructionTranslator<E> by delegator