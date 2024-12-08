#region Assembly NAudio.WinForms, Version=2.1.0.0, Culture=neutral, PublicKeyToken=e279aa5131008a41
// C:\Users\dusti\.nuget\packages\naudio.winforms\2.1.0\lib\netcoreapp3.1\NAudio.WinForms.dll
// Decompiled with ICSharpCode.Decompiler 7.1.0.6543
#endregion

using System;
using System.ComponentModel;
using System.Drawing;
using System.Windows.Forms;
using NAudio.Wave;

namespace NAudio.Gui
{
    public class WaveViewer : UserControl
    {
        private Container components;

        private WaveStream waveStream;

        private int samplesPerPixel = 128;

        private long startPosition;

        private int bytesPerSample;

        public WaveStream WaveStream
        {
            get
            {
                return waveStream;
            }
            set
            {
                waveStream = value;
                if (waveStream != null)
                {
                    bytesPerSample = waveStream.WaveFormat.BitsPerSample / 8 * waveStream.WaveFormat.Channels;
                }

                Invalidate();
            }
        }

        public int SamplesPerPixel
        {
            get
            {
                return samplesPerPixel;
            }
            set
            {
                samplesPerPixel = value;
                Invalidate();
            }
        }

        public long StartPosition
        {
            get
            {
                return startPosition;
            }
            set
            {
                startPosition = value;
            }
        }

        public WaveViewer()
        {
            InitializeComponent();
            DoubleBuffered = true;
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing && components != null)
            {
                components.Dispose();
            }

            base.Dispose(disposing);
        }

        protected override void OnPaint(PaintEventArgs e)
        {
            if (waveStream != null)
            {
                waveStream.Position = 0L;
                byte[] array = new byte[samplesPerPixel * bytesPerSample];
                waveStream.Position = startPosition + e.ClipRectangle.Left * bytesPerSample * samplesPerPixel;
                for (float num = e.ClipRectangle.X; num < (float)e.ClipRectangle.Right; num += 1f)
                {
                    short num2 = 0;
                    short num3 = 0;
                    int num4 = waveStream.Read(array, 0, samplesPerPixel * bytesPerSample);
                    if (num4 == 0)
                    {
                        break;
                    }

                    for (int i = 0; i < num4; i += 2)
                    {
                        short num5 = BitConverter.ToInt16(array, i);
                        if (num5 < num2)
                        {
                            num2 = num5;
                        }

                        if (num5 > num3)
                        {
                            num3 = num5;
                        }
                    }

                    float num6 = ((float)num2 - -32768f) / 65535f;
                    float num7 = ((float)num3 - -32768f) / 65535f;
                    e.Graphics.DrawLine(Pens.Black, num, (float)base.Height * num6, num, (float)base.Height * num7);
                }
            }

            base.OnPaint(e);
        }

        private void InitializeComponent()
        {
            components = new System.ComponentModel.Container();
        }
    }
}
#if false // Decompilation log
'208' items in cache
------------------
Resolve: 'System.Runtime, Version=4.2.2.0, Culture=neutral, PublicKeyToken=b03f5f7f11d50a3a'
Found single assembly: 'System.Runtime, Version=7.0.0.0, Culture=neutral, PublicKeyToken=b03f5f7f11d50a3a'
WARN: Version mismatch. Expected: '4.2.2.0', Got: '7.0.0.0'
Load from: 'C:\Program Files\dotnet\packs\Microsoft.NETCore.App.Ref\7.0.5\ref\net7.0\System.Runtime.dll'
------------------
Resolve: 'NAudio.WinMM, Version=2.1.0.0, Culture=neutral, PublicKeyToken=e279aa5131008a41'
Found single assembly: 'NAudio.WinMM, Version=2.1.0.0, Culture=neutral, PublicKeyToken=e279aa5131008a41'
Load from: 'C:\Users\dusti\.nuget\packages\naudio.winmm\2.1.0\lib\netstandard2.0\NAudio.WinMM.dll'
------------------
Resolve: 'NAudio.Core, Version=2.1.0.0, Culture=neutral, PublicKeyToken=e279aa5131008a41'
Found single assembly: 'NAudio.Core, Version=2.1.0.0, Culture=neutral, PublicKeyToken=e279aa5131008a41'
Load from: 'C:\Users\dusti\.nuget\packages\naudio.core\2.1.0\lib\netstandard2.0\NAudio.Core.dll'
------------------
Resolve: 'System.Threading, Version=4.1.2.0, Culture=neutral, PublicKeyToken=b03f5f7f11d50a3a'
Found single assembly: 'System.Threading, Version=7.0.0.0, Culture=neutral, PublicKeyToken=b03f5f7f11d50a3a'
WARN: Version mismatch. Expected: '4.1.2.0', Got: '7.0.0.0'
Load from: 'C:\Program Files\dotnet\packs\Microsoft.NETCore.App.Ref\7.0.5\ref\net7.0\System.Threading.dll'
------------------
Resolve: 'System.Windows.Forms, Version=4.0.0.0, Culture=neutral, PublicKeyToken=b77a5c561934e089'
Found single assembly: 'System.Windows.Forms, Version=7.0.0.0, Culture=neutral, PublicKeyToken=b77a5c561934e089'
WARN: Version mismatch. Expected: '4.0.0.0', Got: '7.0.0.0'
Load from: 'C:\Program Files\dotnet\packs\Microsoft.WindowsDesktop.App.Ref\7.0.5\ref\net7.0\System.Windows.Forms.dll'
------------------
Resolve: 'System.ComponentModel.Primitives, Version=4.2.2.0, Culture=neutral, PublicKeyToken=b03f5f7f11d50a3a'
Found single assembly: 'System.ComponentModel.Primitives, Version=7.0.0.0, Culture=neutral, PublicKeyToken=b03f5f7f11d50a3a'
WARN: Version mismatch. Expected: '4.2.2.0', Got: '7.0.0.0'
Load from: 'C:\Program Files\dotnet\packs\Microsoft.NETCore.App.Ref\7.0.5\ref\net7.0\System.ComponentModel.Primitives.dll'
------------------
Resolve: 'System.Drawing.Primitives, Version=4.2.1.0, Culture=neutral, PublicKeyToken=b03f5f7f11d50a3a'
Found single assembly: 'System.Drawing.Primitives, Version=7.0.0.0, Culture=neutral, PublicKeyToken=b03f5f7f11d50a3a'
WARN: Version mismatch. Expected: '4.2.1.0', Got: '7.0.0.0'
Load from: 'C:\Program Files\dotnet\packs\Microsoft.NETCore.App.Ref\7.0.5\ref\net7.0\System.Drawing.Primitives.dll'
------------------
Resolve: 'System.Diagnostics.Tools, Version=4.1.2.0, Culture=neutral, PublicKeyToken=b03f5f7f11d50a3a'
Found single assembly: 'System.Diagnostics.Tools, Version=7.0.0.0, Culture=neutral, PublicKeyToken=b03f5f7f11d50a3a'
WARN: Version mismatch. Expected: '4.1.2.0', Got: '7.0.0.0'
Load from: 'C:\Program Files\dotnet\packs\Microsoft.NETCore.App.Ref\7.0.5\ref\net7.0\System.Diagnostics.Tools.dll'
------------------
Resolve: 'System.Diagnostics.Debug, Version=4.1.2.0, Culture=neutral, PublicKeyToken=b03f5f7f11d50a3a'
Found single assembly: 'System.Diagnostics.Debug, Version=7.0.0.0, Culture=neutral, PublicKeyToken=b03f5f7f11d50a3a'
WARN: Version mismatch. Expected: '4.1.2.0', Got: '7.0.0.0'
Load from: 'C:\Program Files\dotnet\packs\Microsoft.NETCore.App.Ref\7.0.5\ref\net7.0\System.Diagnostics.Debug.dll'
------------------
Resolve: 'System.Resources.ResourceManager, Version=4.1.2.0, Culture=neutral, PublicKeyToken=b03f5f7f11d50a3a'
Found single assembly: 'System.Resources.ResourceManager, Version=7.0.0.0, Culture=neutral, PublicKeyToken=b03f5f7f11d50a3a'
WARN: Version mismatch. Expected: '4.1.2.0', Got: '7.0.0.0'
Load from: 'C:\Program Files\dotnet\packs\Microsoft.NETCore.App.Ref\7.0.5\ref\net7.0\System.Resources.ResourceManager.dll'
------------------
Resolve: 'System.ComponentModel.TypeConverter, Version=4.2.2.0, Culture=neutral, PublicKeyToken=b03f5f7f11d50a3a'
Found single assembly: 'System.ComponentModel.TypeConverter, Version=7.0.0.0, Culture=neutral, PublicKeyToken=b03f5f7f11d50a3a'
WARN: Version mismatch. Expected: '4.2.2.0', Got: '7.0.0.0'
Load from: 'C:\Program Files\dotnet\packs\Microsoft.NETCore.App.Ref\7.0.5\ref\net7.0\System.ComponentModel.TypeConverter.dll'
------------------
Resolve: 'System.Drawing.Common, Version=4.0.2.0, Culture=neutral, PublicKeyToken=cc7b13ffcd2ddd51'
Found single assembly: 'System.Drawing.Common, Version=7.0.0.0, Culture=neutral, PublicKeyToken=cc7b13ffcd2ddd51'
WARN: Version mismatch. Expected: '4.0.2.0', Got: '7.0.0.0'
Load from: 'C:\Program Files\dotnet\packs\Microsoft.WindowsDesktop.App.Ref\7.0.5\ref\net7.0\System.Drawing.Common.dll'
------------------
Resolve: 'System.Collections, Version=4.1.2.0, Culture=neutral, PublicKeyToken=b03f5f7f11d50a3a'
Found single assembly: 'System.Collections, Version=7.0.0.0, Culture=neutral, PublicKeyToken=b03f5f7f11d50a3a'
WARN: Version mismatch. Expected: '4.1.2.0', Got: '7.0.0.0'
Load from: 'C:\Program Files\dotnet\packs\Microsoft.NETCore.App.Ref\7.0.5\ref\net7.0\System.Collections.dll'
------------------
Resolve: 'System.Runtime.InteropServices, Version=4.2.2.0, Culture=neutral, PublicKeyToken=b03f5f7f11d50a3a'
Found single assembly: 'System.Runtime.InteropServices, Version=7.0.0.0, Culture=neutral, PublicKeyToken=b03f5f7f11d50a3a'
WARN: Version mismatch. Expected: '4.2.2.0', Got: '7.0.0.0'
Load from: 'C:\Program Files\dotnet\packs\Microsoft.NETCore.App.Ref\7.0.5\ref\net7.0\System.Runtime.InteropServices.dll'
------------------
Resolve: 'System.Runtime.Extensions, Version=4.2.2.0, Culture=neutral, PublicKeyToken=b03f5f7f11d50a3a'
Found single assembly: 'System.Runtime.Extensions, Version=7.0.0.0, Culture=neutral, PublicKeyToken=b03f5f7f11d50a3a'
WARN: Version mismatch. Expected: '4.2.2.0', Got: '7.0.0.0'
Load from: 'C:\Program Files\dotnet\packs\Microsoft.NETCore.App.Ref\7.0.5\ref\net7.0\System.Runtime.Extensions.dll'
------------------
Resolve: 'System.Windows.Forms.Primitives, Version=7.0.0.0, Culture=neutral, PublicKeyToken=b77a5c561934e089'
Found single assembly: 'System.Windows.Forms.Primitives, Version=7.0.0.0, Culture=neutral, PublicKeyToken=b77a5c561934e089'
Load from: 'C:\Program Files\dotnet\packs\Microsoft.WindowsDesktop.App.Ref\7.0.5\ref\net7.0\System.Windows.Forms.Primitives.dll'
------------------
Resolve: 'System.Runtime, Version=7.0.0.0, Culture=neutral, PublicKeyToken=b03f5f7f11d50a3a'
Found single assembly: 'System.Runtime, Version=7.0.0.0, Culture=neutral, PublicKeyToken=b03f5f7f11d50a3a'
Load from: 'C:\Program Files\dotnet\packs\Microsoft.NETCore.App.Ref\7.0.5\ref\net7.0\System.Runtime.dll'
------------------
Resolve: 'System.ComponentModel.Primitives, Version=7.0.0.0, Culture=neutral, PublicKeyToken=b03f5f7f11d50a3a'
Found single assembly: 'System.ComponentModel.Primitives, Version=7.0.0.0, Culture=neutral, PublicKeyToken=b03f5f7f11d50a3a'
Load from: 'C:\Program Files\dotnet\packs\Microsoft.NETCore.App.Ref\7.0.5\ref\net7.0\System.ComponentModel.Primitives.dll'
------------------
Resolve: 'System.ObjectModel, Version=7.0.0.0, Culture=neutral, PublicKeyToken=b03f5f7f11d50a3a'
Found single assembly: 'System.ObjectModel, Version=7.0.0.0, Culture=neutral, PublicKeyToken=b03f5f7f11d50a3a'
Load from: 'C:\Program Files\dotnet\packs\Microsoft.NETCore.App.Ref\7.0.5\ref\net7.0\System.ObjectModel.dll'
------------------
Resolve: 'System.Drawing.Primitives, Version=7.0.0.0, Culture=neutral, PublicKeyToken=b03f5f7f11d50a3a'
Found single assembly: 'System.Drawing.Primitives, Version=7.0.0.0, Culture=neutral, PublicKeyToken=b03f5f7f11d50a3a'
Load from: 'C:\Program Files\dotnet\packs\Microsoft.NETCore.App.Ref\7.0.5\ref\net7.0\System.Drawing.Primitives.dll'
#endif
