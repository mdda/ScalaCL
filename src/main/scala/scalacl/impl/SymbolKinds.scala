/*
 * ScalaCL - putting Scala on the GPU with JavaCL / OpenCL
 * http://scalacl.googlecode.com/
 *
 * Copyright (c) 2009-2013, Olivier Chafik (http://ochafik.com/)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of Olivier Chafik nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY OLIVIER CHAFIK AND CONTRIBUTORS ``AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE REGENTS AND CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package scalacl
package impl

import scalaxy.components.Tuploids
import scalaxy.components.CommonScalaNames

trait SymbolKinds extends Tuploids with CommonScalaNames {
  val global: reflect.api.Universe
  import global._
  import definitions._

  sealed trait SymbolKind
  object SymbolKind {
    case object ArrayLike extends SymbolKind
    case object Tuploid extends SymbolKind
    case object Other extends SymbolKind
  }

  def kindOf(tpe: Type): SymbolKind = {
    kindOf(tpe.typeSymbol, tpe)
  }

  def kindOf(symbol: Symbol): SymbolKind = {
    kindOf(symbol, symbol.typeSignature)
  }

  def kindOf(symbol: Symbol, tpe: Type): SymbolKind = {
    if (tpe != null && (tpe <:< typeOf[CLArray[_]] || tpe <:< typeOf[CLFilteredArray[_]]))
      SymbolKind.ArrayLike
    else if (isTuploidType(tpe))
      SymbolKind.Tuploid
    else
      SymbolKind.Other
  }
}
