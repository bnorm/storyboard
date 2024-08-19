/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.text.diff

/**
 * This class handles sequences of replacements resulting from a comparison.
 *
 *
 * The comparison of two objects sequences leads to the identification of common
 * parts and parts which only belong to the first or to the second sequence. The
 * common parts appear in the edit script in the form of *keep* commands,
 * they can be considered as synchronization objects between the two sequences.
 * These synchronization objects split the two sequences in synchronized
 * sub-sequences. The first sequence can be transformed into the second one by
 * replacing each synchronized sub-sequence of the first sequence by the
 * corresponding sub-sequence of the second sequence. This is a synthetic way to
 * see an [edit script][EditScript], replacing individual
 * [delete][DeleteCommand], [keep][KeepCommand] and
 * [insert][InsertCommand] commands by fewer replacements acting on
 * complete sub-sequences.
 *
 *
 *
 * This class is devoted to perform this interpretation. It visits an
 * [edit script][EditScript] (because it implements the
 * [CommandVisitor] interface) and calls a user-supplied
 * handler implementing the [ReplacementsHandler]
 * interface to process the sub-sequences.
 *
 *
 * @see ReplacementsHandler
 *
 * @see EditScript
 *
 * @see StringsComparator
 *
 *
 * @param <T> object type
 * @since 1.0
</T> */
class ReplacementsFinder<T>(handler: ReplacementsHandler<T>) : CommandVisitor<T> {
    /**
     * List of pending insertions.
     */
    private val pendingInsertions: MutableList<T> = ArrayList<T>()

    /**
     * List of pending deletions.
     */
    private val pendingDeletions: MutableList<T> = ArrayList<T>()

    /**
     * Count of elements skipped.
     */
    private var skipped = 0

    /** Handler to call when synchronized sequences are found.  */
    private val handler: ReplacementsHandler<T> = handler

    /**
     * Add an object to the pending deletions set.
     *
     * @param object  object to delete
     */
    override fun visitDeleteCommand(`object`: T) {
        pendingDeletions.add(`object`)
    }

    /**
     * Add an object to the pending insertions set.
     *
     * @param object  object to insert
     */
    override fun visitInsertCommand(`object`: T) {
        pendingInsertions.add(`object`)
    }

    /**
     * Handle a synchronization object.
     *
     *
     * When a synchronization object is identified, the pending insertions and
     * pending deletions sets are provided to the user handler as subsequences.
     *
     *
     * @param object  synchronization object detected
     */
    override fun visitKeepCommand(`object`: T) {
        if (pendingDeletions.isEmpty() && pendingInsertions.isEmpty()) {
            ++skipped
        } else {
            handler.handleReplacement(skipped, pendingDeletions, pendingInsertions)
            pendingDeletions.clear()
            pendingInsertions.clear()
            skipped = 1
        }
    }
}
